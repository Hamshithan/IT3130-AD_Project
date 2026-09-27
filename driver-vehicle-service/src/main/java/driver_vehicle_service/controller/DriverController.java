package com.example.drivervehicle.controller;

import com.example.drivervehicle.entity.Driver;
import com.example.drivervehicle.service.DriverService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    public ResponseEntity<Driver> createDriver(
            @RequestBody Driver driver) {

        return ResponseEntity.ok(
                driverService.createDriver(driver)
        );
    }

    @GetMapping
    public ResponseEntity<List<Driver>> getAllDrivers() {

        return ResponseEntity.ok(
                driverService.getAllDrivers()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getDriver(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                driverService.getDriverById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Driver> updateDriver(
            @PathVariable Long id,
            @RequestBody Driver driver) {

        return ResponseEntity.ok(
                driverService.updateDriver(id, driver)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(
            @PathVariable Long id) {

        driverService.deleteDriver(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<Driver> updateAvailability(
            @PathVariable Long id,
            @RequestParam boolean available) {

        return ResponseEntity.ok(
                driverService.updateAvailability(id, available)
        );
    }

    @PatchMapping("/{id}/location")
    public ResponseEntity<Driver> updateLocation(
            @PathVariable Long id,
            @RequestParam Double latitude,
            @RequestParam Double longitude) {

        return ResponseEntity.ok(
                driverService.updateLocation(
                        id,
                        latitude,
                        longitude
                )
        );
    }

    @GetMapping("/available")
    public ResponseEntity<List<Driver>> getAvailableDrivers() {

        return ResponseEntity.ok(
                driverService.getAvailableDrivers()
        );
    }

    @GetMapping("/eligible")
    public ResponseEntity<List<Driver>> getEligibleDrivers(
            @RequestParam String serviceArea) {

        return ResponseEntity.ok(
                driverService.getEligibleDrivers(serviceArea)
        );
    }
}