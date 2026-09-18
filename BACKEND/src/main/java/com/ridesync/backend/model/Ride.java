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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rides")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ride_id")
    private Long rideId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "source_location", nullable = false, length = 150)
    private String sourceLocation;

    @Column(name = "destination_location", nullable = false, length = 150)
    private String destinationLocation;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    // DB: TINYINT UNSIGNED — Java Byte maps to JDBC TINYINT
    @Column(name = "total_seats", nullable = false)
    private Byte totalSeats;

    @Column(name = "seats_available", nullable = false)
    private Byte seatsAvailable;

    @Column(name = "fare_per_seat", nullable = false, precision = 8, scale = 2)
    private BigDecimal farePerSeat;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_preference", nullable = false, columnDefinition = "ENUM('ANY','MALE_ONLY','FEMALE_ONLY')")
    private GenderPreference genderPreference = GenderPreference.ANY;

    @Enumerated(EnumType.STRING)
    @Column(name = "ride_status", nullable = false, columnDefinition = "ENUM('SCHEDULED','ONGOING','COMPLETED','CANCELLED')")
    private RideStatus rideStatus = RideStatus.SCHEDULED;

    @Column(name = "additional_notes", length = 255)
    private String additionalNotes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (genderPreference == null) {
            genderPreference = GenderPreference.ANY;
        }
        if (rideStatus == null) {
            rideStatus = RideStatus.SCHEDULED;
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(String sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getTotalSeats() {
        return totalSeats == null ? null : totalSeats.intValue();
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats == null ? null : totalSeats.byteValue();
    }

    public Integer getSeatsAvailable() {
        return seatsAvailable == null ? null : seatsAvailable.intValue();
    }

    public void setSeatsAvailable(Integer seatsAvailable) {
        this.seatsAvailable = seatsAvailable == null ? null : seatsAvailable.byteValue();
    }

    public BigDecimal getFarePerSeat() {
        return farePerSeat;
    }

    public void setFarePerSeat(BigDecimal farePerSeat) {
        this.farePerSeat = farePerSeat;
    }

    public GenderPreference getGenderPreference() {
        return genderPreference;
    }

    public void setGenderPreference(GenderPreference genderPreference) {
        this.genderPreference = genderPreference;
    }

    public RideStatus getRideStatus() {
        return rideStatus;
    }

    public void setRideStatus(RideStatus rideStatus) {
        this.rideStatus = rideStatus;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
