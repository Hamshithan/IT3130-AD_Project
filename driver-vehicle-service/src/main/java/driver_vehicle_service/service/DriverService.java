package com.example.drivervehicle.service;

import com.example.drivervehicle.entity.Driver;
import com.example.drivervehicle.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public Driver createDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Driver getDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Driver not found"));
    }

    public Driver updateDriver(Long id, Driver updatedDriver) {

        Driver driver = getDriverById(id);

        driver.setName(updatedDriver.getName());
        driver.setPhone(updatedDriver.getPhone());
        driver.setLicenseNumber(updatedDriver.getLicenseNumber());
        driver.setAvailable(updatedDriver.isAvailable());
        driver.setServiceArea(updatedDriver.getServiceArea());
        driver.setLatitude(updatedDriver.getLatitude());
        driver.setLongitude(updatedDriver.getLongitude());

        return driverRepository.save(driver);
    }

    public void deleteDriver(Long id) {
        driverRepository.deleteById(id);
    }

    public Driver updateAvailability(Long id, boolean available) {

        Driver driver = getDriverById(id);

        driver.setAvailable(available);

        return driverRepository.save(driver);
    }

    public Driver updateLocation(Long id,
                                 Double latitude,
                                 Double longitude) {

        Driver driver = getDriverById(id);

        driver.setLatitude(latitude);
        driver.setLongitude(longitude);

        return driverRepository.save(driver);
    }

    public List<Driver> getAvailableDrivers() {
        return driverRepository.findByAvailableTrue();
    }

    public List<Driver> getEligibleDrivers(String serviceArea) {

        return driverRepository
                .findByAvailableTrueAndServiceAreaIgnoreCase(serviceArea);
    }
}