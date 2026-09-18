package com.ridesync.backend.dto;

import com.ridesync.backend.model.GenderPreference;
import com.ridesync.backend.model.Ride;
import com.ridesync.backend.model.RideStatus;
import com.ridesync.backend.model.VehicleType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RideResponse {
    private Long rideId;
    private Long driverId;
    private String driverName;
    private BigDecimal driverRating;
    private Long vehicleId;
    private VehicleType vehicleType;
    private String vehicleBrand;
    private String vehicleModel;
    private String source;
    private String destination;
    private LocalDateTime departureTime;
    private Integer totalSeats;
    private Integer seatsAvailable;
    private BigDecimal farePerSeat;
    private GenderPreference genderPreference;
    private RideStatus rideStatus;
    private String additionalNotes;

    public static RideResponse from(Ride ride) {
        RideResponse response = new RideResponse();
        response.rideId = ride.getRideId();
        response.driverId = ride.getDriver().getUserId();
        response.driverName = ride.getDriver().getFullName();
        response.driverRating = ride.getDriver().getAverageRating();
        response.vehicleId = ride.getVehicle().getVehicleId();
        response.vehicleType = ride.getVehicle().getVehicleType();
        response.vehicleBrand = ride.getVehicle().getBrand();
        response.vehicleModel = ride.getVehicle().getModel();
        response.source = ride.getSourceLocation();
        response.destination = ride.getDestinationLocation();
        response.departureTime = ride.getDepartureTime();
        response.totalSeats = ride.getTotalSeats();
        response.seatsAvailable = ride.getSeatsAvailable();
        response.farePerSeat = ride.getFarePerSeat();
        response.genderPreference = ride.getGenderPreference();
        response.rideStatus = ride.getRideStatus();
        response.additionalNotes = ride.getAdditionalNotes();
        return response;
    }

    public Long getRideId() {
        return rideId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public BigDecimal getDriverRating() {
        return driverRating;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public Integer getSeatsAvailable() {
        return seatsAvailable;
    }

    public BigDecimal getFarePerSeat() {
        return farePerSeat;
    }

    public GenderPreference getGenderPreference() {
        return genderPreference;
    }

    public RideStatus getRideStatus() {
        return rideStatus;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }
}
