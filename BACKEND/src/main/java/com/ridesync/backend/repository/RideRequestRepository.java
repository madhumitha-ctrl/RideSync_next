package com.ridesync.backend.repository;

import com.ridesync.backend.model.RideRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RideRequestRepository extends JpaRepository<RideRequest, Long> {

    boolean existsByRideRideIdAndPassengerUserId(Long rideId, Long passengerId);

    @Query("""
            SELECT rr FROM RideRequest rr
            JOIN FETCH rr.ride r
            JOIN FETCH r.driver d
            JOIN FETCH r.vehicle v
            JOIN FETCH rr.passenger p
            WHERE p.userId = :passengerId
            ORDER BY rr.requestedAt DESC
            """)
    List<RideRequest> findBookingsForPassenger(@Param("passengerId") Long passengerId);

    @Query("""
            SELECT rr FROM RideRequest rr
            JOIN FETCH rr.ride r
            JOIN FETCH r.driver d
            JOIN FETCH rr.passenger p
            WHERE d.userId = :driverId
            ORDER BY rr.requestedAt DESC
            """)
    List<RideRequest> findBookingsForDriver(@Param("driverId") Long driverId);

    Optional<RideRequest> findByRideRideIdAndPassengerUserId(Long rideId, Long passengerId);

    long countByPassengerUserId(Long passengerId);
}
