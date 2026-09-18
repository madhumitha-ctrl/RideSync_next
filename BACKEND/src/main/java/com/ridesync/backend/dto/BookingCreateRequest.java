package com.ridesync.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BookingCreateRequest {

    @NotNull(message = "Passenger ID is required")
    private Long passengerId;

    @NotNull(message = "Seats requested is required")
    @Min(value = 1, message = "At least 1 seat required")
    @Max(value = 8, message = "Cannot request more than 8 seats")
    private Integer seatsRequested;

    private String pickupPoint;

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public Integer getSeatsRequested() {
        return seatsRequested;
    }

    public void setSeatsRequested(Integer seatsRequested) {
        this.seatsRequested = seatsRequested;
    }

    public String getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(String pickupPoint) {
        this.pickupPoint = pickupPoint;
    }
}
