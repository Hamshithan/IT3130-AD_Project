package com.ridelink.fare_payment_service.controller;

import com.ridelink.fare_payment_service.dto.LoginRequest;
import com.ridelink.fare_payment_service.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @Valid @RequestBody LoginRequest request) {

        // Temporary demo authentication
        if (!request.getPassword().equals("123456")) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of("error", "Invalid username or password"));
        }

        String role = request.getRole().toUpperCase();

        if (!role.equals("PASSENGER")
                && !role.equals("DRIVER")
                && !role.equals("ADMIN")) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Invalid role"));
        }

        String token = jwtService.generateToken(
                request.getUsername(),
                role
        );

        return ResponseEntity.ok(
                Map.of(
                        "username", request.getUsername(),
                        "role", role,
                        "token", token
                )
        );
    }
}
