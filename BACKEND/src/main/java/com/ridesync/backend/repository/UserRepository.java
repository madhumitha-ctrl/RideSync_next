package com.ridesync.backend.repository;

import com.ridesync.backend.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCollegeId(String collegeId);
    boolean existsByPhoneNumber(String phoneNumber);
}
