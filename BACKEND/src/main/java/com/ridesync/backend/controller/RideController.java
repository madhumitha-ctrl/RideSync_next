package com.ridesync.backend.controller;

import com.ridesync.backend.dto.CreateRideRequest;
import com.ridesync.backend.dto.RideResponse;
import com.ridesync.backend.service.RideService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping
    public RideResponse create(@Valid @RequestBody CreateRideRequest request) {
        return rideService.createRide(request);
    }

    @GetMapping("/search")
    public List<RideResponse> search(
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String date) {
        return rideService.search(source, destination, date);
    }

    @GetMapping("/{id}")
    public RideResponse getById(@PathVariable Long id) {
        return rideService.getRide(id);
    }
}
