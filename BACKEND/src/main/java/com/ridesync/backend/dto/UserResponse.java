package com.ridesync.backend.dto;

import com.ridesync.backend.model.Gender;
import com.ridesync.backend.model.User;
import com.ridesync.backend.model.UserRole;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UserResponse {
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private Gender gender;
    private String collegeId;
    private UserRole role;
    private String profilePicUrl;
    private BigDecimal averageRating;
    private LocalDateTime createdAt;

    public static UserResponse from(User user) {
        UserResponse response = new UserResponse();
        response.userId = user.getUserId();
        response.name = user.getFullName();
        response.email = user.getEmail();
        response.phone = user.getPhoneNumber();
        response.gender = user.getGender();
        response.collegeId = user.getCollegeId();
        response.role = user.getRole();
        response.profilePicUrl = user.getProfilePicUrl();
        response.averageRating = user.getAverageRating();
        response.createdAt = user.getCreatedAt();
        return response;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Gender getGender() {
        return gender;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public UserRole getRole() {
        return role;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
