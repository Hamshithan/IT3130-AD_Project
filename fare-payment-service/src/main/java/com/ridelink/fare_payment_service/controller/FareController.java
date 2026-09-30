package com.ridelink.fare_payment_service.controller;

import com.ridelink.fare_payment_service.dto.FareRequest;
import com.ridelink.fare_payment_service.model.Fare;
import com.ridelink.fare_payment_service.service.FareService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fares")
public class FareController {

    private final FareService fareService;

    public FareController(FareService fareService) {
        this.fareService = fareService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<Fare> calculateFare(
            @Valid @RequestBody FareRequest request) {

        Fare fare = fareService.calculateFare(
                request.getRideId(),
                request.getDistanceKm()
        );

        return ResponseEntity.ok(fare);
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<Fare> getFare(
            @PathVariable String rideId) {

        Fare fare = fareService.getFareByRideId(rideId);

        return ResponseEntity.ok(fare);
    }
}
