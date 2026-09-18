package com.ridesync.backend.service;

import com.ridesync.backend.dto.BookingCreateRequest;
import com.ridesync.backend.dto.BookingResponse;
import com.ridesync.backend.exception.ApiException;
import com.ridesync.backend.model.RequestStatus;
import com.ridesync.backend.model.Ride;
import com.ridesync.backend.model.RideRequest;
import com.ridesync.backend.model.RideStatus;
import com.ridesync.backend.model.User;
import com.ridesync.backend.repository.RideRequestRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    private final RideRequestRepository rideRequestRepository;
    private final RideService rideService;
    private final UserService userService;

    public BookingService(
            RideRequestRepository rideRequestRepository,
            RideService rideService,
            UserService userService) {
        this.rideRequestRepository = rideRequestRepository;
        this.rideService = rideService;
        this.userService = userService;
    }

    @Transactional
    public Map<String, Object> bookRide(Long rideId, BookingCreateRequest request) {
        User passenger = userService.getUserOrThrow(request.getPassengerId());
        Ride ride = rideService.getRideEntity(rideId);

        if (ride.getDriver().getUserId().equals(passenger.getUserId())) {
            throw new ApiException(HttpStatus.BAD_REQUEST.value(), "You cannot book your own ride");
        }
        if (ride.getRideStatus() != RideStatus.SCHEDULED) {
            throw new ApiException(HttpStatus.BAD_REQUEST.value(), "Ride is not available for booking");
        }
        if (ride.getSeatsAvailable() < request.getSeatsRequested()) {
            throw new ApiException(HttpStatus.BAD_REQUEST.value(), "Not enough seats available");
        }
        if (rideRequestRepository.existsByRideRideIdAndPassengerUserId(rideId, passenger.getUserId())) {
            throw new ApiException(HttpStatus.CONFLICT.value(), "You have already booked this ride");
        }

        RideRequest booking = new RideRequest();
        booking.setRide(ride);
        booking.setPassenger(passenger);
        booking.setSeatsRequested(request.getSeatsRequested());
        booking.setPickupPoint(request.getPickupPoint());
        booking.setRequestStatus(RequestStatus.ACCEPTED);
        booking.setRespondedAt(LocalDateTime.now());

        ride.setSeatsAvailable(ride.getSeatsAvailable() - request.getSeatsRequested());
        rideService.save(ride);
        RideRequest saved = rideRequestRepository.save(booking);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Ride booked successfully");
        response.put("bookingId", saved.getRequestId());
        return response;
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getPassengerBookings(Long userId) {
        userService.getUserOrThrow(userId);
        return rideRequestRepository.findBookingsForPassenger(userId).stream()
                .map(BookingResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getDriverBookings(Long driverId) {
        userService.getUserOrThrow(driverId);
        return rideRequestRepository.findBookingsForDriver(driverId).stream()
                .map(BookingResponse::from)
                .toList();
    }
}
