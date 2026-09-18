package com.ridesync.backend.controller;

import com.ridesync.backend.dto.LoginRequest;
import com.ridesync.backend.dto.RegisterRequest;
import com.ridesync.backend.dto.UpdateProfileRequest;
import com.ridesync.backend.dto.UserResponse;
import com.ridesync.backend.service.UserService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@Valid @RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @GetMapping("/{id}")
    public UserResponse getProfile(@PathVariable("id") Long id) {
        return userService.getProfile(id);
    }

    @PutMapping("/{id}")
    public UserResponse updateProfile(
            @PathVariable("id") Long id,
            @RequestBody UpdateProfileRequest request) {
        return userService.updateProfile(id, request);
    }

    @GetMapping("/{id}/stats")
    public Map<String, Object> stats(@PathVariable("id") Long id) {
        return userService.getAccountStats(id);
    }
}
