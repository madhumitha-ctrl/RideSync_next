package com.ridesync.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "ride_requests",
        uniqueConstraints = @UniqueConstraint(name = "uq_ride_requests_ride_passenger", columnNames = {"ride_id", "passenger_id"})
)
public class RideRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ride_id", nullable = false)
    private Ride ride;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "passenger_id", nullable = false)
    private User passenger;

    // DB: TINYINT UNSIGNED — Java Byte maps to JDBC TINYINT (Integer would validate as INTEGER)
    @Column(name = "seats_requested", nullable = false)
    private Byte seatsRequested = (byte) 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status", nullable = false, columnDefinition = "ENUM('PENDING','ACCEPTED','REJECTED','CANCELLED')")
    private RequestStatus requestStatus = RequestStatus.PENDING;

    @Column(name = "pickup_point", length = 150)
    private String pickupPoint;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    @PrePersist
    void onCreate() {
        if (requestedAt == null) {
            requestedAt = LocalDateTime.now();
        }
        if (requestStatus == null) {
            requestStatus = RequestStatus.PENDING;
        }
        if (seatsRequested == null) {
            seatsRequested = (byte) 1;
        }
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public User getPassenger() {
        return passenger;
    }

    public void setPassenger(User passenger) {
        this.passenger = passenger;
    }

    public Integer getSeatsRequested() {
        return seatsRequested == null ? null : seatsRequested.intValue();
    }

    public void setSeatsRequested(Integer seatsRequested) {
        this.seatsRequested = seatsRequested == null ? null : seatsRequested.byteValue();
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(String pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public LocalDateTime getRespondedAt() {
        return respondedAt;
    }

    public void setRespondedAt(LocalDateTime respondedAt) {
        this.respondedAt = respondedAt;
    }
}
