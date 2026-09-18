package com.ridesync.backend.dto;

import com.ridesync.backend.model.VehicleType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VehicleCreateRequest {

    @NotNull(message = "Owner ID is required")
    private Long ownerId;

    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;

    private String brand;
    private String model;

    @NotBlank(message = "Registration number is required")
    private String registrationNo;

    private String color;

    @NotNull(message = "Total seats is required")
    @Min(value = 1, message = "Seats must be at least 1")
    @Max(value = 8, message = "Seats cannot exceed 8")
    private Integer totalSeats;

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }
}
