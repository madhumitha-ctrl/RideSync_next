package com.ridesync.backend.repository;

import com.ridesync.backend.model.Vehicle;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByOwnerUserId(Long ownerId);
    boolean existsByRegistrationNo(String registrationNo);
}
