# RideSync Backend

Spring Boot API for the RideSync campus ride-sharing prototype.

## Overview

- **Stack:** Spring Boot 4.1, Spring Data JPA, MySQL, BCrypt, Gemini (optional)
- **Frontend:** Next.js app in the repository root (`http://localhost:3000`)
- **Database schema:** [RideSync-Database](https://github.com/b-shiva-prasad/RideSync-Database) (source of truth)

## Architecture

```
Controller → Service → Repository → MySQL (ridesync)
```

MVP tables used: `users`, `vehicles`, `rides`, `ride_requests`  
Optional (schema only): `payments`, `reviews`

## Database setup

1. Install MySQL 8+
2. Create database:
   ```sql
   CREATE DATABASE ridesync;
   USE ridesync;
   ```
3. Run the SQL script from the database repository (`ridesync_database (1).sql`)
4. Confirm tables exist: `users`, `vehicles`, `rides`, `ride_requests`, …

## Environment variables

Copy `BACKEND/.env.example` and export before starting:

```bash
export DB_URL=jdbc:mysql://localhost:3306/ridesync
export DB_USERNAME=root
export DB_PASSWORD=your_password
export GEMINI_API_KEY=your_gemini_api_key   # optional
export GEMINI_MODEL=gemini-2.0-flash        # optional
```

`spring.jpa.hibernate.ddl-auto=validate` — Hibernate does **not** create/alter tables.

## Run backend

```bash
cd BACKEND
export JAVA_HOME=...   # JDK 17+
export DB_URL=jdbc:mysql://localhost:3306/ridesync
export DB_USERNAME=root
export DB_PASSWORD=your_password
./mvnw spring-boot:run
```

Health check: `GET http://localhost:8080/hello`

## Run frontend

```bash
# repo root
cp .env.example .env.local   # sets NEXT_PUBLIC_API_URL
npm install
npm run dev
```

Open `http://localhost:3000/login`

## API endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/hello` | Smoke test |
| POST | `/users/register` | Register |
| POST | `/users/login` | Login (BCrypt) |
| GET | `/users/{id}` | Profile |
| PUT | `/users/{id}` | Update profile |
| GET | `/users/{id}/stats` | Ride/booking counts |
| GET | `/users/{id}/bookings` | Passenger booking history |
| GET | `/users/{id}/ride-bookings` | Driver-side bookings |
| POST | `/vehicles` | Create vehicle |
| GET | `/vehicles/user/{userId}` | List user vehicles |
| POST | `/rides` | Post ride (auto-creates vehicle if needed) |
| GET | `/rides/search` | Search scheduled rides |
| GET | `/rides/{id}` | Ride details |
| POST | `/rides/{id}/book` | Book ride (auto-ACCEPTED) |
| GET | `/rides/recommend?userId=` | Rule-based recommendations |
| POST | `/ai/chat` | Gemini chatbot (fallback if no key) |

### Sample requests

**Register**
```bash
curl -X POST http://localhost:8080/users/register \
  -H 'Content-Type: application/json' \
  -d '{"name":"Alice","email":"alice@gcet.edu.in","password":"secret12","phone":"9000000001","gender":"FEMALE","collegeId":"CSE2026001"}'
```

**Login**
```bash
curl -X POST http://localhost:8080/users/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"alice@gcet.edu.in","password":"secret12"}'
```

**Post ride**
```bash
curl -X POST http://localhost:8080/rides \
  -H 'Content-Type: application/json' \
  -d '{"driverId":1,"source":"Campus Main Gate","destination":"City Railway Station","departureTime":"2026-12-01T08:30:00","totalSeats":3,"farePerSeat":50,"vehicleType":"CAR","acAvailable":true}'
```

**Search**
```bash
curl 'http://localhost:8080/rides/search?source=Campus&destination=Railway'
```

**Book**
```bash
curl -X POST http://localhost:8080/rides/1/book \
  -H 'Content-Type: application/json' \
  -d '{"passengerId":2,"seatsRequested":1,"pickupPoint":"Main Gate"}'
```

**AI chat**
```bash
curl -X POST http://localhost:8080/ai/chat \
  -H 'Content-Type: application/json' \
  -d '{"message":"How do I book a ride?"}'
```

## Gemini setup

1. Create an API key in Google AI Studio
2. `export GEMINI_API_KEY=...`
3. Restart backend

If the key is missing or Gemini fails, `/ai/chat` returns a helpful local fallback response.

## Demo flow

1. Register User A → Login → Post Ride  
2. Register User B → Login → Search → Book User A’s ride  
3. Check Booking History (User B)  
4. Open AI Assistant → ask a question  
5. Dashboard recommendation card shows scored rides  

## Tests

```bash
cd BACKEND
./mvnw test
```

Default unit test covers BCrypt hashing (no MySQL required).
