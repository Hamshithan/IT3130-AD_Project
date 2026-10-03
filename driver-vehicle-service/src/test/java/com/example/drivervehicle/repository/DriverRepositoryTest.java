package com.example.drivervehicle.repository;

import com.example.drivervehicle.entity.Driver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DriverRepositoryTest {

    @Autowired
    private DriverRepository driverRepository;

    @BeforeEach
    void setUp() {
        driverRepository.deleteAll();
    }

    @Test
    @DisplayName("Should find available drivers")
    void shouldFindAvailableDrivers() {
        Driver d1 = new Driver("Alice", "1234567890", "LIC-001", true, "Downtown", 10.0, 20.0);
        Driver d2 = new Driver("Bob", "0987654321", "LIC-002", false, "Downtown", 10.1, 20.1);
        driverRepository.save(d1);
        driverRepository.save(d2);

        List<Driver> available = driverRepository.findByAvailableTrue();
        assertThat(available).hasSize(1);
        assertThat(available.get(0).getName()).isEqualTo("Alice");
    }

    @Test
    @DisplayName("Should find available drivers by service area ignoring case")
    void shouldFindAvailableDriversByServiceArea() {
        Driver d1 = new Driver("Alice", "1234567890", "LIC-001", true, "Downtown", 10.0, 20.0);
        Driver d2 = new Driver("Bob", "0987654321", "LIC-002", true, "Uptown", 10.1, 20.1);
        driverRepository.save(d1);
        driverRepository.save(d2);

        List<Driver> result = driverRepository.findByAvailableTrueAndServiceAreaIgnoreCase("downtown");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Alice");
    }

    @Test
    @DisplayName("Should check if license number exists")
    void shouldCheckLicenseExists() {
        Driver d = new Driver("Alice", "1234567890", "LIC-001", true, "Downtown", 10.0, 20.0);
        driverRepository.save(d);

        assertThat(driverRepository.existsByLicenseNumber("LIC-001")).isTrue();
        assertThat(driverRepository.existsByLicenseNumber("LIC-999")).isFalse();
    }
}
