package com.ridelink.ridemanagement.controller;

import com.ridelink.ridemanagement.dto.AssignDriverRequest;
import com.ridelink.ridemanagement.dto.CancelRideRequest;
import com.ridelink.ridemanagement.dto.CreateRideRequest;
import com.ridelink.ridemanagement.dto.RideResponse;
import com.ridelink.ridemanagement.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rides")
@RequiredArgsConstructor
@Tag(name = "Ride Management API", description = "Endpoints for creating, managing, and updating ride lifecycle states")
public class RideController {

    private final RideService rideService;

    @PostMapping
    @Operation(summary = "Create a new ride request", description = "Submits a passenger ride request with pickup and destination details.")
    @ApiResponse(responseCode = "201", description = "Ride created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request payload")
    public ResponseEntity<RideResponse> createRide(@Valid @RequestBody CreateRideRequest request) {
        RideResponse response = rideService.createRide(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{rideId}")
    @Operation(summary = "Get ride by ID", description = "Retrieves current details and lifecycle status of a specific ride.")
    @ApiResponse(responseCode = "200", description = "Ride found")
    @ApiResponse(responseCode = "404", description = "Ride not found")
    public ResponseEntity<RideResponse> getRideById(@PathVariable UUID rideId) {
        RideResponse response = rideService.getRideById(rideId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/passenger/{passengerId}")
    @Operation(summary = "Get passenger rides", description = "Retrieves all ride requests created by a specific passenger.")
    @ApiResponse(responseCode = "200", description = "Rides retrieved successfully")
    public ResponseEntity<List<RideResponse>> getRidesByPassengerId(@PathVariable UUID passengerId) {
        List<RideResponse> responses = rideService.getRidesByPassengerId(passengerId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Get driver rides", description = "Retrieves all rides assigned to or completed by a specific driver.")
    @ApiResponse(responseCode = "200", description = "Rides retrieved successfully")
    public ResponseEntity<List<RideResponse>> getRidesByDriverId(@PathVariable UUID driverId) {
        List<RideResponse> responses = rideService.getRidesByDriverId(driverId);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{rideId}/assign")
    @Operation(summary = "Assign driver to ride", description = "Assigns an available driver to a ride in REQUESTED status.")
    @ApiResponse(responseCode = "200", description = "Driver assigned successfully")
    @ApiResponse(responseCode = "404", description = "Ride not found")
    @ApiResponse(responseCode = "409", description = "Invalid ride status transition")
    public ResponseEntity<RideResponse> assignDriver(
            @PathVariable UUID rideId,
            @Valid @RequestBody AssignDriverRequest request) {
        RideResponse response = rideService.assignDriver(rideId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{rideId}/accept")
    @Operation(summary = "Driver accepts ride", description = "Allows the assigned driver to accept the ride assignment.")
    @ApiResponse(responseCode = "200", description = "Ride accepted successfully")
    @ApiResponse(responseCode = "404", description = "Ride not found")
    @ApiResponse(responseCode = "409", description = "Invalid ride status transition or missing driver")
    public ResponseEntity<RideResponse> acceptRide(@PathVariable UUID rideId) {
        RideResponse response = rideService.acceptRide(rideId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{rideId}/start")
    @Operation(summary = "Start ride", description = "Transitions an ACCEPTED ride to IN_PROGRESS when passenger pickup occurs.")
    @ApiResponse(responseCode = "200", description = "Ride started successfully")
    @ApiResponse(responseCode = "404", description = "Ride not found")
    @ApiResponse(responseCode = "409", description = "Invalid ride status transition")
    public ResponseEntity<RideResponse> startRide(@PathVariable UUID rideId) {
        RideResponse response = rideService.startRide(rideId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{rideId}/complete")
    @Operation(summary = "Complete ride", description = "Marks an IN_PROGRESS ride as COMPLETED when destination is reached.")
    @ApiResponse(responseCode = "200", description = "Ride completed successfully")
    @ApiResponse(responseCode = "404", description = "Ride not found")
    @ApiResponse(responseCode = "409", description = "Invalid ride status transition")
    public ResponseEntity<RideResponse> completeRide(@PathVariable UUID rideId) {
        RideResponse response = rideService.completeRide(rideId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{rideId}/cancel")
    @Operation(summary = "Cancel ride", description = "Cancels an active ride according to business lifecycle rules.")
    @ApiResponse(responseCode = "200", description = "Ride cancelled successfully")
    @ApiResponse(responseCode = "404", description = "Ride not found")
    @ApiResponse(responseCode = "409", description = "Invalid ride status transition")
    public ResponseEntity<RideResponse> cancelRide(
            @PathVariable UUID rideId,
            @RequestBody(required = false) CancelRideRequest request) {
        RideResponse response = rideService.cancelRide(rideId, request);
        return ResponseEntity.ok(response);
    }
}
