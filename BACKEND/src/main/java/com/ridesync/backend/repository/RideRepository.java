package com.ridesync.backend.repository;

import com.ridesync.backend.model.Ride;
import com.ridesync.backend.model.RideStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RideRepository extends JpaRepository<Ride, Long> {

    @Query("""
            SELECT r FROM Ride r
            JOIN FETCH r.driver d
            JOIN FETCH r.vehicle v
            WHERE r.rideStatus = :status
              AND r.seatsAvailable > 0
              AND LOWER(r.sourceLocation) LIKE LOWER(CONCAT('%', :source, '%'))
              AND LOWER(r.destinationLocation) LIKE LOWER(CONCAT('%', :destination, '%'))
              AND (:from IS NULL OR r.departureTime >= :from)
              AND (:to IS NULL OR r.departureTime < :to)
            ORDER BY r.departureTime ASC
            """)
    List<Ride> searchAvailable(
            @Param("source") String source,
            @Param("destination") String destination,
            @Param("status") RideStatus status,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("""
            SELECT r FROM Ride r
            JOIN FETCH r.driver d
            JOIN FETCH r.vehicle v
            WHERE r.rideStatus = com.ridesync.backend.model.RideStatus.SCHEDULED
              AND r.seatsAvailable > 0
              AND r.departureTime >= :now
            ORDER BY r.departureTime ASC
            """)
    List<Ride> findUpcomingAvailable(@Param("now") LocalDateTime now);

    long countByDriverUserId(Long driverId);
}
