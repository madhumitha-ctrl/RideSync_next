package com.ridesync.backend.service;

import com.ridesync.backend.dto.VehicleCreateRequest;
import com.ridesync.backend.dto.VehicleResponse;
import com.ridesync.backend.exception.ApiException;
import com.ridesync.backend.model.User;
import com.ridesync.backend.model.Vehicle;
import com.ridesync.backend.model.VehicleType;
import com.ridesync.backend.repository.VehicleRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserService userService;

    public VehicleService(VehicleRepository vehicleRepository, UserService userService) {
        this.vehicleRepository = vehicleRepository;
        this.userService = userService;
    }

    @Transactional
    public VehicleResponse create(VehicleCreateRequest request) {
        User owner = userService.getUserOrThrow(request.getOwnerId());

        String registrationNo = request.getRegistrationNo().trim().toUpperCase();
        if (vehicleRepository.existsByRegistrationNo(registrationNo)) {
            throw new ApiException(HttpStatus.CONFLICT.value(), "Registration number already exists");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setOwner(owner);
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setBrand(defaultIfBlank(request.getBrand(), "Generic"));
        vehicle.setModel(defaultIfBlank(request.getModel(), request.getVehicleType().name()));
        vehicle.setRegistrationNo(registrationNo);
        vehicle.setColor(defaultIfBlank(request.getColor(), "Unspecified"));
        vehicle.setTotalSeats(request.getTotalSeats());

        return VehicleResponse.from(vehicleRepository.save(vehicle));
    }

    @Transactional(readOnly = true)
    public List<VehicleResponse> listByUser(Long userId) {
        userService.getUserOrThrow(userId);
        return vehicleRepository.findByOwnerUserId(userId).stream()
                .map(VehicleResponse::from)
                .toList();
    }

    public Vehicle getOwnedVehicleOrThrow(Long vehicleId, Long ownerId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND.value(), "Vehicle not found"));
        if (!vehicle.getOwner().getUserId().equals(ownerId)) {
            throw new ApiException(HttpStatus.BAD_REQUEST.value(), "Vehicle does not belong to this driver");
        }
        return vehicle;
    }

    @Transactional
    public Vehicle createDefaultVehicle(User owner, VehicleType type, Integer seats, Boolean acAvailable) {
        VehicleType vehicleType = type != null ? type : VehicleType.CAR;
        int totalSeats = seats != null && seats > 0 ? seats : 3;

        VehicleCreateRequest request = new VehicleCreateRequest();
        request.setOwnerId(owner.getUserId());
        request.setVehicleType(vehicleType);
        request.setBrand("RideSync");
        request.setModel(vehicleType.name());
        request.setRegistrationNo(("RS" + owner.getUserId() + UUID.randomUUID().toString().substring(0, 6)).toUpperCase());
        request.setColor(Boolean.TRUE.equals(acAvailable) ? "AC" : "Standard");
        request.setTotalSeats(Math.min(Math.max(totalSeats, 1), 8));
        return vehicleRepository.findById(create(request).getVehicleId()).orElseThrow();
    }

    private String defaultIfBlank(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }
}
