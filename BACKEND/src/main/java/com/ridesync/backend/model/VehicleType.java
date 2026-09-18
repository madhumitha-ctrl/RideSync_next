package com.ridesync.backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum VehicleType {
    CAR,
    BIKE,
    SCOOTER,
    VAN;

    @JsonCreator
    public static VehicleType from(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String normalized = value.trim().toUpperCase();
        if ("ALL".equals(normalized)) {
            return null;
        }
        return VehicleType.valueOf(normalized);
    }
}
