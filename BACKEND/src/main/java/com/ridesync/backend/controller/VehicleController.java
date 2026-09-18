package com.ridesync.backend.controller;

import com.ridesync.backend.dto.VehicleCreateRequest;
import com.ridesync.backend.dto.VehicleResponse;
import com.ridesync.backend.service.VehicleService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public VehicleResponse create(@Valid @RequestBody VehicleCreateRequest request) {
        return vehicleService.create(request);
    }

    @GetMapping("/user/{userId}")
    public List<VehicleResponse> listByUser(@PathVariable Long userId) {
        return vehicleService.listByUser(userId);
    }
}
