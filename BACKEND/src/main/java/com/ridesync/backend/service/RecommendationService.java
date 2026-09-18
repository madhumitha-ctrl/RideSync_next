package com.ridesync.backend.service;

import com.ridesync.backend.dto.RecommendationResponse;
import com.ridesync.backend.model.Ride;
import com.ridesync.backend.model.User;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecommendationService {

    private final RideService rideService;
    private final UserService userService;

    public RecommendationService(RideService rideService, UserService userService) {
        this.rideService = rideService;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<RecommendationResponse> recommend(Long userId) {
        User user = userService.getUserOrThrow(userId);
        List<Ride> rides = rideService.findUpcomingAvailable();

        List<RecommendationResponse> results = new ArrayList<>();
        for (Ride ride : rides) {
            if (ride.getDriver().getUserId().equals(user.getUserId())) {
                continue;
            }

            int score = 40;
            List<String> reasons = new ArrayList<>();

            if (ride.getSeatsAvailable() >= 2) {
                score += 20;
                reasons.add("multiple seats available");
            } else if (ride.getSeatsAvailable() == 1) {
                score += 10;
                reasons.add("last seat available");
            }

            long hoursUntil = Duration.between(LocalDateTime.now(), ride.getDepartureTime()).toHours();
            if (hoursUntil >= 0 && hoursUntil <= 24) {
                score += 20;
                reasons.add("departing soon");
            } else if (hoursUntil <= 72) {
                score += 10;
                reasons.add("upcoming departure");
            }

            if (ride.getFarePerSeat().compareTo(BigDecimal.valueOf(100)) <= 0) {
                score += 15;
                reasons.add("affordable fare");
            }

            if (ride.getDriver().getAverageRating() != null
                    && ride.getDriver().getAverageRating().compareTo(BigDecimal.valueOf(4.0)) >= 0) {
                score += 15;
                reasons.add("highly rated driver");
            }

            score = Math.min(score, 99);

            RecommendationResponse item = new RecommendationResponse();
            item.setRideId(ride.getRideId());
            item.setSource(ride.getSourceLocation());
            item.setDestination(ride.getDestinationLocation());
            item.setFare(ride.getFarePerSeat());
            item.setAvailableSeats(ride.getSeatsAvailable());
            item.setDriverName(ride.getDriver().getFullName());
            item.setVehicleType(ride.getVehicle().getVehicleType());
            item.setDepartureTime(ride.getDepartureTime());
            item.setScore(score);
            item.setReason(reasons.isEmpty()
                    ? "Available scheduled ride with open seats."
                    : "Matches availability: " + String.join(", ", reasons) + ".");
            results.add(item);
        }

        results.sort(Comparator.comparingInt(RecommendationResponse::getScore).reversed());
        return results.stream().limit(5).toList();
    }
}
