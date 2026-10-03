package com.example.drivervehicle.repository;

import com.example.drivervehicle.entity.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    @BeforeEach
    void setUp() {
        vehicleRepository.deleteAll();
    }

    @Test
    @DisplayName("Should find vehicle by driver ID")
    void shouldFindVehicleByDriverId() {
        Vehicle v = new Vehicle(1L, "SEDAN", "Toyota", "Camry", "REG-100", "Black");
        vehicleRepository.save(v);

        Optional<Vehicle> found = vehicleRepository.findByDriverId(1L);
        assertThat(found).isPresent();
        assertThat(found.get().getRegistrationNumber()).isEqualTo("REG-100");
    }

    @Test
    @DisplayName("Should check if registration number exists")
    void shouldCheckRegistrationExists() {
        Vehicle v = new Vehicle(1L, "SEDAN", "Toyota", "Camry", "REG-100", "Black");
        vehicleRepository.save(v);

        assertThat(vehicleRepository.existsByRegistrationNumber("REG-100")).isTrue();
        assertThat(vehicleRepository.existsByRegistrationNumber("REG-999")).isFalse();
    }
}
