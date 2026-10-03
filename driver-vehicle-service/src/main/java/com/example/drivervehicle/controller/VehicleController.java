package com.example.drivervehicle.controller;

import com.example.drivervehicle.dto.VehicleCreateRequest;
import com.example.drivervehicle.dto.VehicleResponse;
import com.example.drivervehicle.dto.VehicleUpdateRequest;
import com.example.drivervehicle.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Vehicle Management", description = "APIs for managing vehicle details and driver-vehicle associations")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    @Operation(summary = "Register a new vehicle", description = "Creates a vehicle record assigned to a driver ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vehicle registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "404", description = "Driver not found"),
            @ApiResponse(responseCode = "409", description = "Registration number already exists")
    })
    public ResponseEntity<VehicleResponse> createVehicle(@Valid @RequestBody VehicleCreateRequest request) {
        VehicleResponse response = vehicleService.createVehicle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all vehicles", description = "Retrieves all registered vehicle records.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved vehicle list")
    public ResponseEntity<List<VehicleResponse>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicleResponses());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by ID", description = "Retrieves vehicle details by vehicle ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle details retrieved"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    public ResponseEntity<VehicleResponse> getVehicle(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleResponseById(id));
    }

    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Get vehicle by driver ID", description = "Retrieves the vehicle associated with a specific driver ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle details retrieved"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found for driver")
    })
    public ResponseEntity<VehicleResponse> getVehicleByDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(vehicleService.getVehicleResponseByDriverId(driverId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update vehicle details", description = "Updates details of an existing vehicle record.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "404", description = "Vehicle or Driver not found"),
            @ApiResponse(responseCode = "409", description = "Registration number conflicts with another vehicle")
    })
    public ResponseEntity<VehicleResponse> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody VehicleUpdateRequest request) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete vehicle", description = "Deletes a vehicle record by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vehicle deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}