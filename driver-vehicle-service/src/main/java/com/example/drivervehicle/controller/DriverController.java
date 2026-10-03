package com.example.drivervehicle.controller;

import com.example.drivervehicle.dto.*;
import com.example.drivervehicle.entity.Driver;
import com.example.drivervehicle.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@Tag(name = "Driver Management", description = "APIs for managing driver profiles, availability, service areas, and locations")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    @Operation(summary = "Register driver profile", description = "Creates a new driver profile with name, phone, license number, service area, and optional location.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Driver created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "409", description = "Driver with given license number already exists")
    })
    public ResponseEntity<DriverResponse> createDriver(@Valid @RequestBody DriverCreateRequest request) {
        DriverResponse response = driverService.createDriver(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all drivers", description = "Retrieves a list of all registered drivers.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved driver list")
    public ResponseEntity<List<DriverResponse>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDriverResponses());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get driver by ID", description = "Retrieves specific driver details by driver ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Driver found"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    public ResponseEntity<DriverResponse> getDriver(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.getDriverResponseById(id));
    }

    @GetMapping("/{id}/with-vehicle")
    @Operation(summary = "Get driver profile with vehicle details", description = "Retrieves driver details along with their registered vehicle information.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Driver details with vehicle retrieved"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    public ResponseEntity<DriverWithVehicleResponse> getDriverWithVehicle(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.getDriverWithVehicle(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update driver profile", description = "Updates an existing driver profile.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Driver updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "404", description = "Driver not found"),
            @ApiResponse(responseCode = "409", description = "License number conflicts with another driver")
    })
    public ResponseEntity<DriverResponse> updateDriver(
            @PathVariable Long id,
            @Valid @RequestBody DriverUpdateRequest request) {
        return ResponseEntity.ok(driverService.updateDriver(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete driver profile", description = "Deletes a driver profile and associated vehicle record.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Driver deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/availability")
    @Operation(summary = "Update driver availability", description = "Toggles driver availability online/offline status.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Availability updated successfully"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    public ResponseEntity<DriverResponse> updateAvailability(
            @PathVariable Long id,
            @Parameter(description = "Availability status") @RequestParam(required = false) Boolean available,
            @RequestBody(required = false) DriverAvailabilityRequest request) {
        boolean isAvailable = (available != null) ? available : (request != null && Boolean.TRUE.equals(request.getAvailable()));
        return ResponseEntity.ok(driverService.updateAvailabilityResponse(id, isAvailable));
    }

    @PatchMapping("/{id}/location")
    @Operation(summary = "Update simulated driver location", description = "Updates latitude and longitude of simulated driver position.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Location updated successfully"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    public ResponseEntity<DriverResponse> updateLocation(
            @PathVariable Long id,
            @Parameter(description = "Latitude degree (-90 to 90)") @RequestParam(required = false) Double latitude,
            @Parameter(description = "Longitude degree (-180 to 180)") @RequestParam(required = false) Double longitude,
            @RequestBody(required = false) DriverLocationRequest request) {
        Double lat = (latitude != null) ? latitude : (request != null ? request.getLatitude() : null);
        Double lng = (longitude != null) ? longitude : (request != null ? request.getLongitude() : null);
        return ResponseEntity.ok(driverService.updateLocationResponse(id, lat, lng));
    }

    @GetMapping("/available")
    @Operation(summary = "Get available drivers", description = "Retrieves all drivers who are currently marked as available.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved available drivers")
    public ResponseEntity<List<DriverResponse>> getAvailableDrivers() {
        return ResponseEntity.ok(driverService.getAvailableDriverResponses());
    }

    @GetMapping("/eligible")
    @Operation(summary = "Retrieve eligible available drivers", description = "Filters drivers who are available and assigned to the given service area.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved eligible drivers")
    public ResponseEntity<List<DriverResponse>> getEligibleDrivers(
            @Parameter(description = "Service area name (e.g. Downtown, Uptown)") @RequestParam(required = false) String serviceArea) {
        return ResponseEntity.ok(driverService.getEligibleDriverResponses(serviceArea));
    }
}