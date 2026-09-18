package com.ridesync.backend.service;

import com.ridesync.backend.dto.LoginRequest;
import com.ridesync.backend.dto.RegisterRequest;
import com.ridesync.backend.dto.UpdateProfileRequest;
import com.ridesync.backend.dto.UserResponse;
import com.ridesync.backend.exception.ApiException;
import com.ridesync.backend.model.User;
import com.ridesync.backend.model.UserRole;
import com.ridesync.backend.repository.RideRepository;
import com.ridesync.backend.repository.RideRequestRepository;
import com.ridesync.backend.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RideRepository rideRepository;
    private final RideRequestRepository rideRequestRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            RideRepository rideRepository,
            RideRequestRepository rideRequestRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rideRepository = rideRepository;
        this.rideRequestRepository = rideRequestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Map<String, Object> register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        String phone = request.getPhone().trim();
        String collegeId = request.getCollegeId().trim();

        if (userRepository.existsByEmail(email)) {
            throw new ApiException(HttpStatus.CONFLICT.value(), "Email already registered");
        }
        if (userRepository.existsByCollegeId(collegeId)) {
            throw new ApiException(HttpStatus.CONFLICT.value(), "College ID already registered");
        }
        if (userRepository.existsByPhoneNumber(phone)) {
            throw new ApiException(HttpStatus.CONFLICT.value(), "Phone number already registered");
        }

        User user = new User();
        user.setFullName(request.getName().trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(phone);
        user.setGender(request.getGender());
        user.setCollegeId(collegeId);
        user.setRole(UserRole.STUDENT);

        User saved = userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Registration successful");
        response.put("userId", saved.getUserId());
        return response;
    }

    public Map<String, Object> login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED.value(), "Invalid email or password"));

        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED.value(), "Account is inactive");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED.value(), "Invalid email or password");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Login successful");
        response.put("user", UserResponse.from(user));
        return response;
    }

    public UserResponse getProfile(Long userId) {
        return UserResponse.from(getUserOrThrow(userId));
    }

    @Transactional
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = getUserOrThrow(userId);

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setFullName(request.getName().trim());
        }
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            String phone = request.getPhone().trim();
            if (!phone.equals(user.getPhoneNumber()) && userRepository.existsByPhoneNumber(phone)) {
                throw new ApiException(HttpStatus.CONFLICT.value(), "Phone number already registered");
            }
            user.setPhoneNumber(phone);
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getProfilePicUrl() != null) {
            user.setProfilePicUrl(request.getProfilePicUrl());
        }

        return UserResponse.from(userRepository.save(user));
    }

    public Map<String, Object> getAccountStats(Long userId) {
        getUserOrThrow(userId);
        Map<String, Object> stats = new HashMap<>();
        stats.put("ridesPosted", rideRepository.countByDriverUserId(userId));
        stats.put("totalBookings", rideRequestRepository.countByPassengerUserId(userId));
        return stats;
    }

    public User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND.value(), "User not found"));
    }
}
