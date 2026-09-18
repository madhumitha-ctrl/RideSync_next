package com.ridesync.backend.dto;

import com.ridesync.backend.model.GenderPreference;
import com.ridesync.backend.model.VehicleType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateRideRequest {

    @NotNull(message = "Driver ID is required")
    private Long driverId;

    private Long vehicleId;

    @NotBlank(message = "Source is required")
    private String source;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotNull(message = "Departure time is required")
    private LocalDateTime departureTime;

    @NotNull(message = "Total seats is required")
    @Min(value = 1, message = "Seats must be at least 1")
    @Max(value = 8, message = "Seats cannot exceed 8")
    private Integer totalSeats;

    @NotNull(message = "Fare is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Fare cannot be negative")
    private BigDecimal farePerSeat;

    private GenderPreference genderPreference;
    private String additionalNotes;

    // Used when vehicleId is omitted — create a default vehicle from the form
    private VehicleType vehicleType;
    private Boolean acAvailable;

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
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

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Boolean getAcAvailable() {
        return acAvailable;
    }

    public void setAcAvailable(Boolean acAvailable) {
        this.acAvailable = acAvailable;
    }
}
