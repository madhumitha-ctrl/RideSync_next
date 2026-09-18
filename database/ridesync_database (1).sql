-- ============================================================================
-- RIDESYNC - SMART CAMPUS RIDE SHARING SYSTEM
-- Production-style MySQL Database Script
-- ============================================================================
-- Author : Database Developer
-- Engine : MySQL 8.0+
-- Usage  : Run this entire script inside MySQL Workbench after USE ridesync;
-- ============================================================================

USE ridesync;

-- Safety: drop tables if re-running this script during development.
-- Order matters: children before parents (reverse of creation order).
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS ride_requests;
DROP TABLE IF EXISTS rides;
DROP TABLE IF EXISTS vehicles;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================================
-- TABLE 1: users
-- ----------------------------------------------------------------------------
-- WHY THIS TABLE EXISTS:
-- Central identity table for everyone on the platform. Both ride owners
-- (drivers) and ride seekers (passengers) are stored here as the same
-- entity type, since any student can act as either role depending on the
-- ride. This avoids duplicating profile data across separate "Driver" and
-- "Passenger" tables (a common over-normalization mistake) and matches how
-- Spring Security/JPA typically model a single authenticatable User entity.
-- ============================================================================
CREATE TABLE users (
    user_id         BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    full_name       VARCHAR(100)        NOT NULL,
    email           VARCHAR(150)        NOT NULL,
    password_hash   VARCHAR(255)        NOT NULL COMMENT 'BCrypt hash, never store plain text',
    phone_number    VARCHAR(15)         NOT NULL,
    gender          ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
    college_id      VARCHAR(30)         NOT NULL COMMENT 'Student/Staff campus ID number',
    role            ENUM('STUDENT', 'STAFF', 'ADMIN') NOT NULL DEFAULT 'STUDENT',
    profile_pic_url VARCHAR(255)        NULL,
    average_rating  DECIMAL(3,2)        NOT NULL DEFAULT 0.00 COMMENT 'Denormalized cache of avg rating, updated via app logic or trigger',
    is_active       BOOLEAN             NOT NULL DEFAULT TRUE COMMENT 'Soft-delete / account disable flag',
    created_at      TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_users_email      UNIQUE (email),
    CONSTRAINT uq_users_college_id UNIQUE (college_id),
    CONSTRAINT uq_users_phone      UNIQUE (phone_number),
    CONSTRAINT chk_users_rating_range CHECK (average_rating BETWEEN 0.00 AND 5.00)
) ENGINE=InnoDB COMMENT='Stores all registered platform users (students/staff who can drive or ride)';


-- ============================================================================
-- TABLE 2: vehicles
-- ----------------------------------------------------------------------------
-- WHY THIS TABLE EXISTS:
-- A user who offers rides needs a vehicle on record. Separating vehicles
-- from users (1-to-many: a user may own more than one vehicle, e.g. a bike
-- and a car) keeps vehicle attributes (plate number, capacity, type) out of
-- the users table, satisfying 2NF/3NF — vehicle details don't depend on the
-- user's identity, only on the vehicle itself.
-- ============================================================================
CREATE TABLE vehicles (
    vehicle_id      BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    owner_id        BIGINT UNSIGNED     NOT NULL,
    vehicle_type    ENUM('CAR', 'BIKE', 'SCOOTER', 'VAN') NOT NULL,
    brand           VARCHAR(50)         NOT NULL,
    model           VARCHAR(50)         NOT NULL,
    registration_no VARCHAR(20)         NOT NULL,
    color           VARCHAR(30)         NULL,
    total_seats     TINYINT UNSIGNED    NOT NULL COMMENT 'Total passenger seats excluding driver',
    created_at      TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_vehicles_registration_no UNIQUE (registration_no),
    CONSTRAINT fk_vehicles_owner FOREIGN KEY (owner_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT chk_vehicles_seats_positive CHECK (total_seats BETWEEN 1 AND 8)
) ENGINE=InnoDB COMMENT='Vehicles owned by users, used when they post rides as a driver';


-- ============================================================================
-- TABLE 3: rides
-- ----------------------------------------------------------------------------
-- WHY THIS TABLE EXISTS:
-- The core entity of the platform. Each row is one ride offer posted by a
-- driver: route, timing, fare, seat count, and preferences. seats_available
-- is tracked here (denormalized counter) rather than always counting
-- accepted ride_requests, because checking/decrementing a single integer
-- under a transaction is simpler and faster for seat-locking logic in
-- Spring Boot than aggregating child rows on every booking attempt.
-- ============================================================================
CREATE TABLE rides (
    ride_id             BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    driver_id           BIGINT UNSIGNED     NOT NULL,
    vehicle_id          BIGINT UNSIGNED     NOT NULL,
    source_location     VARCHAR(150)        NOT NULL,
    destination_location VARCHAR(150)       NOT NULL,
    departure_time      DATETIME            NOT NULL,
    total_seats         TINYINT UNSIGNED    NOT NULL,
    seats_available     TINYINT UNSIGNED    NOT NULL,
    fare_per_seat        DECIMAL(8,2)        NOT NULL,
    gender_preference    ENUM('ANY', 'MALE_ONLY', 'FEMALE_ONLY') NOT NULL DEFAULT 'ANY',
    ride_status          ENUM('SCHEDULED', 'ONGOING', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'SCHEDULED',
    additional_notes     VARCHAR(255)        NULL,
    created_at           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_rides_driver FOREIGN KEY (driver_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_rides_vehicle FOREIGN KEY (vehicle_id)
        REFERENCES vehicles(vehicle_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT chk_rides_fare_positive CHECK (fare_per_seat >= 0),
    CONSTRAINT chk_rides_seats_available_range CHECK (seats_available >= 0 AND seats_available <= total_seats),
    CONSTRAINT chk_rides_total_seats_positive CHECK (total_seats BETWEEN 1 AND 8)
) ENGINE=InnoDB COMMENT='Ride offers posted by drivers, with route, timing, fare and seat availability';


-- ============================================================================
-- TABLE 4: ride_requests
-- ----------------------------------------------------------------------------
-- WHY THIS TABLE EXISTS:
-- Represents a passenger's request to join a specific ride, and the
-- driver's decision on it. This is intentionally its own table (not a
-- column on rides or users) because it models a many-to-many relationship
-- between users and rides — many passengers can request many rides — and
-- carries its own lifecycle state (PENDING/ACCEPTED/REJECTED/CANCELLED)
-- independent of the ride's own status.
-- ============================================================================
CREATE TABLE ride_requests (
    request_id      BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    ride_id         BIGINT UNSIGNED     NOT NULL,
    passenger_id    BIGINT UNSIGNED     NOT NULL,
    seats_requested TINYINT UNSIGNED    NOT NULL DEFAULT 1,
    request_status  ENUM('PENDING', 'ACCEPTED', 'REJECTED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    pickup_point    VARCHAR(150)        NULL,
    requested_at    TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    responded_at    TIMESTAMP           NULL COMMENT 'Set when driver accepts/rejects',

    -- A passenger can request a given ride only once
    CONSTRAINT uq_ride_requests_ride_passenger UNIQUE (ride_id, passenger_id),
    CONSTRAINT fk_requests_ride FOREIGN KEY (ride_id)
        REFERENCES rides(ride_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_requests_passenger FOREIGN KEY (passenger_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT chk_requests_seats_positive CHECK (seats_requested BETWEEN 1 AND 8)
) ENGINE=InnoDB COMMENT='Join requests made by passengers for a ride, and the driver decision lifecycle';


-- ============================================================================
-- TABLE 5: payments
-- ----------------------------------------------------------------------------
-- WHY THIS TABLE EXISTS:
-- Optional payment tracking, kept separate from ride_requests because not
-- every accepted request necessarily gets paid through the platform (cash
-- splits are common in campus carpooling), and a payment has its own
-- attributes (method, transaction reference, paid timestamp) that would
-- otherwise force nullable columns onto every ride_requests row. One
-- payment row per accepted request (1-to-1), modeled as its own table to
-- keep ride_requests focused purely on the request/approval workflow.
-- ============================================================================
CREATE TABLE payments (
    payment_id      BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    request_id      BIGINT UNSIGNED     NOT NULL,
    amount          DECIMAL(8,2)        NOT NULL,
    payment_method  ENUM('CASH', 'UPI', 'CARD', 'WALLET') NOT NULL DEFAULT 'CASH',
    payment_status  ENUM('PENDING', 'PAID', 'FAILED', 'REFUNDED') NOT NULL DEFAULT 'PENDING',
    transaction_ref VARCHAR(100)        NULL COMMENT 'External gateway reference, NULL for cash',
    paid_at         TIMESTAMP           NULL,
    created_at      TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_payments_request UNIQUE (request_id),
    CONSTRAINT fk_payments_request FOREIGN KEY (request_id)
        REFERENCES ride_requests(request_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT chk_payments_amount_positive CHECK (amount >= 0)
) ENGINE=InnoDB COMMENT='Optional fare payment records tied one-to-one with an accepted ride request';


-- ============================================================================
-- TABLE 6: reviews
-- ----------------------------------------------------------------------------
-- WHY THIS TABLE EXISTS:
-- Captures post-ride feedback between users. It is tied to a specific ride
-- so reviews can only be left for rides that actually happened, and it
-- records both who wrote the review and who it is about, since either a
-- passenger can rate a driver or a driver can rate a passenger. This is
-- kept independent of ride_requests/rides so a single ride (which may have
-- multiple passengers) can carry several independent review rows.
-- ============================================================================
CREATE TABLE reviews (
    review_id       BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    ride_id         BIGINT UNSIGNED     NOT NULL,
    reviewer_id     BIGINT UNSIGNED     NOT NULL COMMENT 'User who wrote the review',
    reviewee_id     BIGINT UNSIGNED     NOT NULL COMMENT 'User being reviewed',
    rating          TINYINT UNSIGNED    NOT NULL,
    comments        VARCHAR(500)        NULL,
    created_at      TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- One review per person per ride
    CONSTRAINT uq_reviews_ride_reviewer_reviewee UNIQUE (ride_id, reviewer_id, reviewee_id),
    CONSTRAINT fk_reviews_ride FOREIGN KEY (ride_id)
        REFERENCES rides(ride_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_reviews_reviewer FOREIGN KEY (reviewer_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_reviews_reviewee FOREIGN KEY (reviewee_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT chk_reviews_rating_range CHECK (rating BETWEEN 1 AND 5)
    -- NOTE: a CHECK (reviewer_id <> reviewee_id) "no self-review" rule was
    -- intentionally left out. MySQL forbids a CHECK constraint on a column
    -- that also has ON DELETE/ON UPDATE CASCADE via a foreign key (Error 3823).
    -- Enforce "no self-review" in the Spring Boot service layer instead.
) ENGINE=InnoDB COMMENT='Post-ride star ratings and comments exchanged between drivers and passengers';


-- ============================================================================
-- HELPFUL INDEXES (beyond what UNIQUE/FK already create)
-- ----------------------------------------------------------------------------
-- These speed up the most common app queries: searching rides by route and
-- date, and listing a user's requests/reviews.
-- ============================================================================
CREATE INDEX idx_rides_search ON rides (source_location, destination_location, departure_time);
CREATE INDEX idx_rides_status ON rides (ride_status);
CREATE INDEX idx_requests_passenger ON ride_requests (passenger_id);
CREATE INDEX idx_reviews_reviewee ON reviews (reviewee_id);


-- ============================================================================
-- SAMPLE DATA
-- ============================================================================

-- ---- USERS ----
INSERT INTO users (full_name, email, password_hash, phone_number, gender, college_id, role, average_rating) VALUES
('Aarav Sharma',   'aarav.sharma@college.edu',   '$2a$10$hash1examplehashvalue', '9876543210', 'MALE',   'CSE2022001', 'STUDENT', 4.50),
('Priya Nair',     'priya.nair@college.edu',     '$2a$10$hash2examplehashvalue', '9876543211', 'FEMALE', 'ECE2022014', 'STUDENT', 4.80),
('Rohan Mehta',    'rohan.mehta@college.edu',    '$2a$10$hash3examplehashvalue', '9876543212', 'MALE',   'MECH2021045','STUDENT', 4.20),
('Sneha Iyer',     'sneha.iyer@college.edu',     '$2a$10$hash4examplehashvalue', '9876543213', 'FEMALE', 'CSE2023032', 'STUDENT', 0.00),
('Karthik Raj',    'karthik.raj@college.edu',    '$2a$10$hash5examplehashvalue', '9876543214', 'MALE',   'CIVIL2020011','STUDENT', 4.00),
('Dr. Anjali Rao',  'anjali.rao@college.edu',     '$2a$10$hash6examplehashvalue', '9876543215', 'FEMALE', 'STAFF0098', 'STAFF',   5.00),
('Admin User',     'admin@college.edu',          '$2a$10$hash7examplehashvalue', '9876543216', 'OTHER',  'ADMIN0001', 'ADMIN',   0.00);

-- ---- VEHICLES ----
INSERT INTO vehicles (owner_id, vehicle_type, brand, model, registration_no, color, total_seats) VALUES
(1, 'CAR',    'Maruti Suzuki', 'Swift',   'KA01AB1234', 'White', 3),
(3, 'CAR',    'Hyundai',       'i20',     'KA02CD5678', 'Red',   3),
(5, 'BIKE',   'Honda',         'Activa',  'KA03EF9012', 'Black', 1),
(6, 'CAR',    'Toyota',        'Innova',  'KA04GH3456', 'Silver',6);

-- ---- RIDES ----
INSERT INTO rides (driver_id, vehicle_id, source_location, destination_location, departure_time, total_seats, seats_available, fare_per_seat, gender_preference, ride_status, additional_notes) VALUES
(1, 1, 'Campus Main Gate',   'City Railway Station', '2026-07-01 08:00:00', 3, 1, 50.00, 'ANY',         'SCHEDULED', 'AC car, can adjust luggage'),
(3, 2, 'Hostel Block C',     'Tech Park Phase 2',    '2026-06-20 09:30:00', 3, 3, 40.00, 'ANY',         'SCHEDULED', NULL),
(5, 3, 'Campus Main Gate',   'Central Mall',         '2026-07-02 18:00:00', 1, 0, 20.00, 'MALE_ONLY',   'SCHEDULED', 'Bike ride, helmet provided'),
(6, 4, 'Faculty Quarters',   'Airport Terminal 1',   '2026-06-25 05:00:00', 6, 4, 150.00,'FEMALE_ONLY', 'COMPLETED', 'Early morning airport drop'),
(3, 2, 'Hostel Block C',     'City Mall',            '2026-07-03 17:00:00', 3, 3, 35.00, 'ANY',         'SCHEDULED', 'Evening ride, AC car');

-- ---- RIDE REQUESTS ----
INSERT INTO ride_requests (ride_id, passenger_id, seats_requested, request_status, pickup_point, responded_at) VALUES
(1, 2, 1, 'ACCEPTED',  'Library Stop',        '2026-06-30 10:00:00'),
(1, 4, 1, 'PENDING',   'Hostel Block A',       NULL),
(2, 4, 1, 'PENDING',   'Hostel Block C Gate',  NULL),
(3, 1, 1, 'ACCEPTED',  'Campus Main Gate',     '2026-07-01 12:00:00'),
(4, 2, 2, 'ACCEPTED',  'Faculty Quarters Gate','2026-06-24 20:00:00'),
(1, 5, 1, 'PENDING',   'Tech Park Gate',       NULL);

-- ---- PAYMENTS ----
INSERT INTO payments (request_id, amount, payment_method, payment_status, transaction_ref, paid_at) VALUES
(1, 50.00,  'UPI',  'PAID',    'TXN1001REF', '2026-06-30 10:05:00'),
(4, 20.00,  'CASH', 'PAID',    NULL,         '2026-07-02 18:30:00'),
(5, 300.00, 'CARD', 'PAID',    'TXN1002REF', '2026-06-24 20:10:00');

-- ---- REVIEWS ----
INSERT INTO reviews (ride_id, reviewer_id, reviewee_id, rating, comments) VALUES
(4, 2, 6, 5, 'Excellent driver, very punctual and friendly!'),
(4, 6, 2, 5, 'Great passenger, ready on time.'),
(3, 1, 5, 4, 'Smooth ride, would book again.');


-- ============================================================================
-- STRUCTURE VERIFICATION COMMANDS
-- ============================================================================
SHOW TABLES;
DESCRIBE users;
DESCRIBE vehicles;
DESCRIBE rides;
DESCRIBE ride_requests;
DESCRIBE payments;
DESCRIBE reviews;
SHOW CREATE TABLE rides;
SHOW INDEX FROM rides;


-- ============================================================================
-- SELECT QUERIES (15+ demonstrating the schema works)
-- ============================================================================

-- 1. List all active users
SELECT user_id, full_name, email, role FROM users WHERE is_active = TRUE;

-- 2. Find a user by college ID (login-style lookup)
SELECT user_id, full_name, email, role FROM users WHERE college_id = 'CSE2022001';

-- 3. List all rides that are still scheduled and have seats available
SELECT ride_id, source_location, destination_location, departure_time, seats_available
FROM rides
WHERE ride_status = 'SCHEDULED' AND seats_available > 0;

-- 4. Search rides between two locations (typical "search ride" feature)
SELECT ride_id, source_location, destination_location, departure_time, fare_per_seat
FROM rides
WHERE source_location = 'Campus Main Gate'
  AND destination_location = 'City Railway Station'
  AND departure_time >= '2026-07-01 00:00:00';

-- 5. List rides with a specific gender preference
SELECT ride_id, source_location, destination_location, gender_preference
FROM rides
WHERE gender_preference = 'FEMALE_ONLY';

-- 6. Show all vehicles belonging to a specific user
SELECT v.vehicle_id, v.brand, v.model, v.registration_no, v.total_seats
FROM vehicles v
WHERE v.owner_id = 1;

-- 7. Show all pending ride requests for a driver to review
SELECT rr.request_id, rr.seats_requested, u.full_name AS passenger_name, rr.pickup_point
FROM ride_requests rr
JOIN rides r ON rr.ride_id = r.ride_id
JOIN users u ON rr.passenger_id = u.user_id
WHERE r.driver_id = 1 AND rr.request_status = 'PENDING';

-- 8. Get full ride details with driver name and vehicle info (JOIN across 3 tables)
SELECT r.ride_id, u.full_name AS driver_name, v.brand, v.model,
       r.source_location, r.destination_location, r.departure_time, r.fare_per_seat
FROM rides r
JOIN users u ON r.driver_id = u.user_id
JOIN vehicles v ON r.vehicle_id = v.vehicle_id
ORDER BY r.departure_time;

-- 9. Get all requests made by a particular passenger, with ride info (JOIN)
SELECT rr.request_id, r.source_location, r.destination_location, r.departure_time, rr.request_status
FROM ride_requests rr
JOIN rides r ON rr.ride_id = r.ride_id
WHERE rr.passenger_id = 4;

-- 10. List accepted passengers for a given ride, with their contact info (JOIN)
SELECT u.full_name, u.phone_number, rr.seats_requested, rr.pickup_point
FROM ride_requests rr
JOIN users u ON rr.passenger_id = u.user_id
WHERE rr.ride_id = 1 AND rr.request_status = 'ACCEPTED';

-- 11. Calculate total earnings per driver from accepted + paid requests (JOIN + aggregate)
SELECT u.full_name AS driver_name, SUM(p.amount) AS total_earnings
FROM payments p
JOIN ride_requests rr ON p.request_id = rr.request_id
JOIN rides r ON rr.ride_id = r.ride_id
JOIN users u ON r.driver_id = u.user_id
WHERE p.payment_status = 'PAID'
GROUP BY u.full_name;

-- 12. Average rating received by each user (JOIN + aggregate)
SELECT u.full_name, ROUND(AVG(rev.rating), 2) AS avg_rating, COUNT(rev.review_id) AS total_reviews
FROM reviews rev
JOIN users u ON rev.reviewee_id = u.user_id
GROUP BY u.full_name;

-- 13. List all reviews for a specific driver, with reviewer names (JOIN)
SELECT u.full_name AS reviewer_name, rev.rating, rev.comments, rev.created_at
FROM reviews rev
JOIN users u ON rev.reviewer_id = u.user_id
WHERE rev.reviewee_id = 6;

-- 14. Show rides that completed but have unpaid accepted requests (multi-JOIN business check)
SELECT r.ride_id, u.full_name AS passenger_name, p.payment_status
FROM rides r
JOIN ride_requests rr ON r.ride_id = rr.ride_id
JOIN users u ON rr.passenger_id = u.user_id
LEFT JOIN payments p ON p.request_id = rr.request_id
WHERE r.ride_status = 'COMPLETED'
  AND rr.request_status = 'ACCEPTED'
  AND (p.payment_status IS NULL OR p.payment_status <> 'PAID');

-- 15. Count how many rides each driver has posted (JOIN + aggregate)
SELECT u.full_name AS driver_name, COUNT(r.ride_id) AS rides_posted
FROM users u
LEFT JOIN rides r ON u.user_id = r.driver_id
GROUP BY u.full_name;

-- 16. Find the cheapest available ride for a given route
SELECT ride_id, fare_per_seat
FROM rides
WHERE source_location = 'Campus Main Gate' AND seats_available > 0
ORDER BY fare_per_seat ASC
LIMIT 1;

-- 17. List passengers who have never left a review (LEFT JOIN ... NULL check)
SELECT u.user_id, u.full_name
FROM users u
LEFT JOIN reviews rev ON u.user_id = rev.reviewer_id
WHERE rev.review_id IS NULL AND u.role = 'STUDENT';

-- 18. Total seats requested vs available, per ride (subquery use)
SELECT r.ride_id, r.total_seats,
       (SELECT COALESCE(SUM(rr.seats_requested), 0)
        FROM ride_requests rr
        WHERE rr.ride_id = r.ride_id AND rr.request_status = 'ACCEPTED') AS seats_booked,
       r.seats_available
FROM rides r;


-- ============================================================================
-- UPDATE EXAMPLES
-- ============================================================================

-- A) Driver accepts a pending request: update request status and decrement seats
--    (run as a transaction in the application layer; shown here as two statements)
UPDATE ride_requests
SET request_status = 'ACCEPTED', responded_at = NOW()
WHERE request_id = 2;

UPDATE rides
SET seats_available = seats_available - 1
WHERE ride_id = 1 AND seats_available > 0;

-- B) Driver rejects a request
UPDATE ride_requests
SET request_status = 'REJECTED', responded_at = NOW()
WHERE request_id = 3;

-- C) Mark a ride as completed once it's done
UPDATE rides
SET ride_status = 'COMPLETED'
WHERE ride_id = 2 AND departure_time < NOW();

-- D) Update a user's average rating after a new review (recalculated from reviews table)
UPDATE users u
SET u.average_rating = (
    SELECT ROUND(AVG(rev.rating), 2)
    FROM reviews rev
    WHERE rev.reviewee_id = u.user_id
)
WHERE u.user_id = 6;

-- E) User updates their own profile phone number
UPDATE users
SET phone_number = '9999999999'
WHERE user_id = 4;


-- ============================================================================
-- DELETE EXAMPLES
-- ============================================================================

-- A) Passenger cancels their own pending request
--    request_id = 6 is the new request from passenger 5 on ride 1, never
--    touched by the UPDATE examples above, so it is still PENDING here.
DELETE FROM ride_requests
WHERE request_id = 6 AND request_status = 'PENDING';

-- B) Driver cancels a ride that has no accepted passengers yet
--    Ride 5 has zero ride_requests at all, so this will succeed immediately.
--    Wrapped in a derived table because MySQL does not allow a subquery to
--    select directly from the same table targeted by DELETE.
DELETE FROM rides
WHERE ride_id = 5
  AND ride_id NOT IN (
      SELECT ride_id FROM (
          SELECT rr.ride_id
          FROM ride_requests rr
          WHERE rr.request_status = 'ACCEPTED'
      ) AS accepted_rides
  );

-- C) Remove a vehicle no longer in use.
--    NOTE: vehicle_id = 3 is still referenced by ride_id = 3 (rides.vehicle_id
--    has ON DELETE RESTRICT), so running this as-is will correctly raise a
--    foreign key constraint error -- that is expected, working behavior,
--    not a bug. It stops a driver from deleting a vehicle that's still
--    attached to a live ride. To actually free up vehicle 3, first remove
--    or reassign the ride that uses it:
--
-- DELETE FROM rides WHERE ride_id = 3;   -- removes the only ride using vehicle 3
-- DELETE FROM vehicles WHERE vehicle_id = 3;   -- now succeeds

-- Example of a delete that succeeds immediately: vehicle 4 is owned by user 6
-- and used by ride 4 (already COMPLETED). Deleting it is blocked the same way
-- to protect ride history, demonstrating RESTRICT is enforced consistently:
DELETE FROM vehicles WHERE vehicle_id = 4;
-- Expected result: ERROR 1451 (23000) - Cannot delete or update a parent row:
-- a foreign key constraint fails (ridesync.rides, CONSTRAINT fk_rides_vehicle)
