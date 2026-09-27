package com.example.drivervehicle.controller;

import com.example.drivervehicle.entity.Vehicle;
import com.example.drivervehicle.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(
            @RequestBody Vehicle vehicle) {

        return ResponseEntity.ok(
                vehicleService.createVehicle(vehicle)
        );
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {

        return ResponseEntity.ok(
                vehicleService.getAllVehicles()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicle(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                vehicleService.getVehicleById(id)
        );
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<Vehicle> getVehicleByDriver(
            @PathVariable Long driverId) {

        return ResponseEntity.ok(
                vehicleService.getVehicleByDriverId(driverId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(
            @PathVariable Long id,
            @RequestBody Vehicle vehicle) {

        return ResponseEntity.ok(
                vehicleService.updateVehicle(id, vehicle)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable Long id) {

        vehicleService.deleteVehicle(id);

        return ResponseEntity.noContent().build();
    }
}