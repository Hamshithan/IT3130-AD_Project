package com.example.drivervehicle.service;

import com.example.drivervehicle.dto.VehicleCreateRequest;
import com.example.drivervehicle.dto.VehicleResponse;
import com.example.drivervehicle.dto.VehicleUpdateRequest;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private VehicleService vehicleService;

    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle(1L, "SUV", "Honda", "CR-V", "REG-777", "Silver");
        vehicle.setId(10L);
    }

    @Test
    @DisplayName("Create vehicle successfully via DTO")
    void createVehicle_Success() {
        VehicleCreateRequest request = new VehicleCreateRequest(1L, "SUV", "Honda", "CR-V", "REG-777", "Silver");
        when(driverRepository.existsById(1L)).thenReturn(true);
        when(vehicleRepository.existsByRegistrationNumber("REG-777")).thenReturn(false);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        VehicleResponse response = vehicleService.createVehicle(request);

        assertThat(response).isNotNull();
        assertThat(response.getRegistrationNumber()).isEqualTo("REG-777");
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Create vehicle throws NotFound if driver does not exist")
    void createVehicle_DriverNotFound() {
        VehicleCreateRequest request = new VehicleCreateRequest(99L, "SUV", "Honda", "CR-V", "REG-777", "Silver");
        when(driverRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> vehicleService.createVehicle(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Driver not found");
    }

    @Test
    @DisplayName("Create vehicle throws Duplicate if registration number exists")
    void createVehicle_DuplicateRegistration() {
        VehicleCreateRequest request = new VehicleCreateRequest(1L, "SUV", "Honda", "CR-V", "REG-777", "Silver");
        when(driverRepository.existsById(1L)).thenReturn(true);
        when(vehicleRepository.existsByRegistrationNumber("REG-777")).thenReturn(true);

        assertThatThrownBy(() -> vehicleService.createVehicle(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("REG-777");
    }

    @Test
    @DisplayName("Get vehicle by driver ID successfully")
    void getVehicleByDriverId_Success() {
        when(vehicleRepository.findByDriverId(1L)).thenReturn(Optional.of(vehicle));

        Vehicle found = vehicleService.getVehicleByDriverId(1L);

        assertThat(found.getRegistrationNumber()).isEqualTo("REG-777");
    }
}
