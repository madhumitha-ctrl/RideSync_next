package com.ridesync.backend.controller;

import com.ridesync.backend.dto.BookingCreateRequest;
import com.ridesync.backend.dto.BookingResponse;
import com.ridesync.backend.service.BookingService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/rides/{id}/book")
    public Map<String, Object> book(
            @PathVariable("id") Long rideId,
            @Valid @RequestBody BookingCreateRequest request) {
        return bookingService.bookRide(rideId, request);
    }

    @GetMapping("/users/{id}/bookings")
    public List<BookingResponse> passengerBookings(@PathVariable("id") Long userId) {
        return bookingService.getPassengerBookings(userId);
    }

    @GetMapping("/users/{id}/ride-bookings")
    public List<BookingResponse> driverBookings(@PathVariable("id") Long userId) {
        return bookingService.getDriverBookings(userId);
    }
}
