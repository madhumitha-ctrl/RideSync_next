package com.ridesync.backend.service;

import com.ridesync.backend.dto.CreateRideRequest;
import com.ridesync.backend.dto.RideResponse;
import com.ridesync.backend.exception.ApiException;
import com.ridesync.backend.model.GenderPreference;
import com.ridesync.backend.model.Ride;
import com.ridesync.backend.model.RideStatus;
import com.ridesync.backend.model.User;
import com.ridesync.backend.model.Vehicle;
import com.ridesync.backend.repository.RideRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final UserService userService;
    private final VehicleService vehicleService;

    public RideService(RideRepository rideRepository, UserService userService, VehicleService vehicleService) {
        this.rideRepository = rideRepository;
        this.userService = userService;
        this.vehicleService = vehicleService;
    }

    @Transactional
    public RideResponse createRide(CreateRideRequest request) {
        User driver = userService.getUserOrThrow(request.getDriverId());

        if (request.getDepartureTime() == null || request.getDepartureTime().isBefore(LocalDateTime.now().minusMinutes(5))) {
            throw new ApiException(HttpStatus.BAD_REQUEST.value(), "Departure time must be in the future");
        }

        Vehicle vehicle;
        if (request.getVehicleId() != null) {
            vehicle = vehicleService.getOwnedVehicleOrThrow(request.getVehicleId(), driver.getUserId());
        } else {
            vehicle = vehicleService.createDefaultVehicle(
                    driver,
                    request.getVehicleType(),
                    request.getTotalSeats(),
                    request.getAcAvailable());
        }

        String notes = request.getAdditionalNotes();
        if (Boolean.TRUE.equals(request.getAcAvailable())) {
            notes = (notes == null || notes.isBlank()) ? "AC available" : notes + " | AC available";
        }

        Ride ride = new Ride();
        ride.setDriver(driver);
        ride.setVehicle(vehicle);
        ride.setSourceLocation(request.getSource().trim());
        ride.setDestinationLocation(request.getDestination().trim());
        ride.setDepartureTime(request.getDepartureTime());
        ride.setTotalSeats(request.getTotalSeats());
        ride.setSeatsAvailable(request.getTotalSeats());
        ride.setFarePerSeat(request.getFarePerSeat());
        ride.setGenderPreference(request.getGenderPreference() != null ? request.getGenderPreference() : GenderPreference.ANY);
        ride.setRideStatus(RideStatus.SCHEDULED);
        ride.setAdditionalNotes(notes);

        return RideResponse.from(rideRepository.save(ride));
    }

    public List<RideResponse> search(String source, String destination, String date) {
        String sourceQuery = source == null ? "" : source.trim();
        String destinationQuery = destination == null ? "" : destination.trim();

        LocalDateTime from = null;
        LocalDateTime to = null;
        if (date != null && !date.isBlank()) {
            LocalDate localDate = LocalDate.parse(date);
            from = localDate.atStartOfDay();
            to = localDate.plusDays(1).atStartOfDay();
        }

        return rideRepository.searchAvailable(sourceQuery, destinationQuery, RideStatus.SCHEDULED, from, to)
                .stream()
                .map(RideResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public RideResponse getRide(Long rideId) {
        return RideResponse.from(getRideEntity(rideId));
    }

    public Ride getRideEntity(Long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND.value(), "Ride not found"));
    }

    public Ride save(Ride ride) {
        return rideRepository.save(ride);
    }

    public List<Ride> findUpcomingAvailable() {
        return rideRepository.findUpcomingAvailable(LocalDateTime.now());
    }
}
