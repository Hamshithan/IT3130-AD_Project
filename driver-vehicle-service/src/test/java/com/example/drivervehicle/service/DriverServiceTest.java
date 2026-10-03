package com.example.drivervehicle.service;

import com.example.drivervehicle.dto.DriverCreateRequest;
import com.example.drivervehicle.dto.DriverResponse;
import com.example.drivervehicle.dto.DriverUpdateRequest;
import com.example.drivervehicle.dto.DriverWithVehicleResponse;
import com.example.drivervehicle.entity.Driver;
import com.example.drivervehicle.entity.Vehicle;
import com.example.drivervehicle.exception.DuplicateResourceException;
import com.example.drivervehicle.exception.ResourceNotFoundException;
import com.example.drivervehicle.repository.DriverRepository;
import com.example.drivervehicle.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private DriverService driverService;

    private Driver driver;

    @BeforeEach
    void setUp() {
        driver = new Driver("John Doe", "1234567890", "LIC-123", true, "Central", 12.34, 56.78);
        driver.setId(1L);
    }

    @Test
    @DisplayName("Create driver successfully via DTO")
    void createDriver_Success() {
        DriverCreateRequest request = new DriverCreateRequest("John Doe", "1234567890", "LIC-123", true, "Central", 12.34, 56.78);
        when(driverRepository.existsByLicenseNumber("LIC-123")).thenReturn(false);
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        DriverResponse response = driverService.createDriver(request);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("John Doe");
        assertThat(response.getLicenseNumber()).isEqualTo("LIC-123");
        verify(driverRepository).save(any(Driver.class));
    }

    @Test
    @DisplayName("Create driver throws exception when license exists")
    void createDriver_DuplicateLicense() {
        DriverCreateRequest request = new DriverCreateRequest("John Doe", "1234567890", "LIC-123", true, "Central", 12.34, 56.78);
        when(driverRepository.existsByLicenseNumber("LIC-123")).thenReturn(true);

        assertThatThrownBy(() -> driverService.createDriver(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("LIC-123");
    }

    @Test
    @DisplayName("Get driver by ID successfully")
    void getDriverById_Success() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        Driver found = driverService.getDriverById(1L);
        assertThat(found.getName()).isEqualTo("John Doe");
    }

    @Test
    @DisplayName("Get driver by ID throws NotFound Exception")
    void getDriverById_NotFound() {
        when(driverRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> driverService.getDriverById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Update driver availability")
    void updateAvailability_Success() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        Driver updated = driverService.updateAvailability(1L, false);

        assertThat(updated.isAvailable()).isFalse();
    }

    @Test
    @DisplayName("Update driver location")
    void updateLocation_Success() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        Driver updated = driverService.updateLocation(1L, 1.23, 4.56);

        assertThat(updated.getLatitude()).isEqualTo(1.23);
        assertThat(updated.getLongitude()).isEqualTo(4.56);
    }

    @Test
    @DisplayName("Get eligible drivers by service area")
    void getEligibleDrivers_Success() {
        when(driverRepository.findByAvailableTrueAndServiceAreaIgnoreCase("Central")).thenReturn(List.of(driver));

        List<Driver> eligible = driverService.getEligibleDrivers("Central");

        assertThat(eligible).hasSize(1);
        assertThat(eligible.get(0).getServiceArea()).isEqualTo("Central");
    }

    @Test
    @DisplayName("Get driver with vehicle details")
    void getDriverWithVehicle_Success() {
        Vehicle vehicle = new Vehicle(1L, "SEDAN", "Toyota", "Corolla", "REG-555", "Red");
        vehicle.setId(10L);

        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(vehicleRepository.findByDriverId(1L)).thenReturn(Optional.of(vehicle));

        DriverWithVehicleResponse response = driverService.getDriverWithVehicle(1L);

        assertThat(response.getDriver().getId()).isEqualTo(1L);
        assertThat(response.getVehicle().getRegistrationNumber()).isEqualTo("REG-555");
    }
}
