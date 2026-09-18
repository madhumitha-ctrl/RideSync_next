package com.ridesync.backend.dto;

import com.ridesync.backend.model.Vehicle;
import com.ridesync.backend.model.VehicleType;

public class VehicleResponse {
    private Long vehicleId;
    private Long ownerId;
    private VehicleType vehicleType;
    private String brand;
    private String model;
    private String registrationNo;
    private String color;
    private Integer totalSeats;

    public static VehicleResponse from(Vehicle vehicle) {
        VehicleResponse response = new VehicleResponse();
        response.vehicleId = vehicle.getVehicleId();
        response.ownerId = vehicle.getOwner().getUserId();
        response.vehicleType = vehicle.getVehicleType();
        response.brand = vehicle.getBrand();
        response.model = vehicle.getModel();
        response.registrationNo = vehicle.getRegistrationNo();
        response.color = vehicle.getColor();
        response.totalSeats = vehicle.getTotalSeats();
        return response;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public String getColor() {
        return color;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }
}
