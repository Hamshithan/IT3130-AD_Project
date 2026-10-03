package com.example.drivervehicle.service;

import com.example.drivervehicle.dto.*;
import com.example.drivervehicle.entity.Driver;
import com.example.drivervehicle.entity.Vehicle;
import com.example.drivervehicle.exception.DuplicateResourceException;
import com.example.drivervehicle.exception.ResourceNotFoundException;
import com.example.drivervehicle.repository.DriverRepository;
import com.example.drivervehicle.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DriverService {

    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    public DriverService(DriverRepository driverRepository, VehicleRepository vehicleRepository) {
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public Driver createDriver(Driver driver) {
        if (driver.getLicenseNumber() != null && driverRepository.existsByLicenseNumber(driver.getLicenseNumber())) {
            throw new DuplicateResourceException("Driver with license number '" + driver.getLicenseNumber() + "' already exists");
        }
        return driverRepository.save(driver);
    }

    public DriverResponse createDriver(DriverCreateRequest request) {
        if (driverRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new DuplicateResourceException("Driver with license number '" + request.getLicenseNumber() + "' already exists");
        }
        Driver driver = new Driver(
                request.getName(),
                request.getPhone(),
                request.getLicenseNumber(),
                request.isAvailable(),
                request.getServiceArea(),
                request.getLatitude(),
                request.getLongitude()
        );
        Driver saved = driverRepository.save(driver);
        return DriverResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<DriverResponse> getAllDriverResponses() {
        return driverRepository.findAll().stream()
                .map(DriverResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Driver getDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
    }

    @Transactional(readOnly = true)
    public DriverResponse getDriverResponseById(Long id) {
        Driver driver = getDriverById(id);
        return DriverResponse.fromEntity(driver);
    }

    public Driver updateDriver(Long id, Driver updatedDriver) {
        Driver driver = getDriverById(id);
        if (updatedDriver.getLicenseNumber() != null &&
                driverRepository.existsByLicenseNumberAndIdNot(updatedDriver.getLicenseNumber(), id)) {
            throw new DuplicateResourceException("Driver with license number '" + updatedDriver.getLicenseNumber() + "' already exists");
        }
        driver.setName(updatedDriver.getName());
        driver.setPhone(updatedDriver.getPhone());
        driver.setLicenseNumber(updatedDriver.getLicenseNumber());
        driver.setAvailable(updatedDriver.isAvailable());
        driver.setServiceArea(updatedDriver.getServiceArea());
        driver.setLatitude(updatedDriver.getLatitude());
        driver.setLongitude(updatedDriver.getLongitude());

        return driverRepository.save(driver);
    }

    public DriverResponse updateDriver(Long id, DriverUpdateRequest request) {
        Driver driver = getDriverById(id);
        if (driverRepository.existsByLicenseNumberAndIdNot(request.getLicenseNumber(), id)) {
            throw new DuplicateResourceException("Driver with license number '" + request.getLicenseNumber() + "' already exists");
        }
        driver.setName(request.getName());
        driver.setPhone(request.getPhone());
        driver.setLicenseNumber(request.getLicenseNumber());
        driver.setAvailable(request.isAvailable());
        driver.setServiceArea(request.getServiceArea());
        driver.setLatitude(request.getLatitude());
        driver.setLongitude(request.getLongitude());

        Driver saved = driverRepository.save(driver);
        return DriverResponse.fromEntity(saved);
    }

    public void deleteDriver(Long id) {
        Driver driver = getDriverById(id);
        vehicleRepository.findByDriverId(id).ifPresent(v -> vehicleRepository.delete(v));
        driverRepository.delete(driver);
    }

    public Driver updateAvailability(Long id, boolean available) {
        Driver driver = getDriverById(id);
        driver.setAvailable(available);
        return driverRepository.save(driver);
    }

    public DriverResponse updateAvailabilityResponse(Long id, boolean available) {
        Driver updated = updateAvailability(id, available);
        return DriverResponse.fromEntity(updated);
    }

    public Driver updateLocation(Long id, Double latitude, Double longitude) {
        Driver driver = getDriverById(id);
        driver.setLatitude(latitude);
        driver.setLongitude(longitude);
        return driverRepository.save(driver);
    }

    public DriverResponse updateLocationResponse(Long id, Double latitude, Double longitude) {
        Driver updated = updateLocation(id, latitude, longitude);
        return DriverResponse.fromEntity(updated);
    }

    @Transactional(readOnly = true)
    public List<Driver> getAvailableDrivers() {
        return driverRepository.findByAvailableTrue();
    }

    @Transactional(readOnly = true)
    public List<DriverResponse> getAvailableDriverResponses() {
        return driverRepository.findByAvailableTrue().stream()
                .map(DriverResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Driver> getEligibleDrivers(String serviceArea) {
        if (serviceArea == null || serviceArea.trim().isEmpty()) {
            return getAvailableDrivers();
        }
        return driverRepository.findByAvailableTrueAndServiceAreaIgnoreCase(serviceArea.trim());
    }

    @Transactional(readOnly = true)
    public List<DriverResponse> getEligibleDriverResponses(String serviceArea) {
        return getEligibleDrivers(serviceArea).stream()
                .map(DriverResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DriverWithVehicleResponse getDriverWithVehicle(Long id) {
        Driver driver = getDriverById(id);
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByDriverId(id);
        return DriverWithVehicleResponse.of(driver, vehicleOpt.orElse(null));
    }
}