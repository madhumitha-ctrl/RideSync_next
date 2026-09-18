package com.ridesync.backend.dto;

import com.ridesync.backend.model.RequestStatus;
import com.ridesync.backend.model.RideRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingResponse {
    private Long bookingId;
    private Long rideId;
    private String source;
    private String destination;
    private LocalDateTime departureTime;
    private String driverName;
    private String passengerName;
    private Integer seatsRequested;
    private BigDecimal fare;
    private RequestStatus status;
    private String pickupPoint;

    public static BookingResponse from(RideRequest request) {
        BookingResponse response = new BookingResponse();
        response.bookingId = request.getRequestId();
        response.rideId = request.getRide().getRideId();
        response.source = request.getRide().getSourceLocation();
        response.destination = request.getRide().getDestinationLocation();
        response.departureTime = request.getRide().getDepartureTime();
        response.driverName = request.getRide().getDriver().getFullName();
        response.passengerName = request.getPassenger().getFullName();
        response.seatsRequested = request.getSeatsRequested();
        response.fare = request.getRide().getFarePerSeat()
                .multiply(BigDecimal.valueOf(request.getSeatsRequested()));
        response.status = request.getRequestStatus();
        response.pickupPoint = request.getPickupPoint();
        return response;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public Long getRideId() {
        return rideId;
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

    public String getDriverName() {
        return driverName;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public Integer getSeatsRequested() {
        return seatsRequested;
    }

    public BigDecimal getFare() {
        return fare;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public String getPickupPoint() {
        return pickupPoint;
    }
}
