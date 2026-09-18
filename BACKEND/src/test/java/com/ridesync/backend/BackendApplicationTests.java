package com.ridesync.backend;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class BackendApplicationTests {

    @Test
    void bcryptPasswordHashingWorks() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("secret123");
        assertTrue(encoder.matches("secret123", hash));
        assertFalse(encoder.matches("wrong", hash));
    }
}
