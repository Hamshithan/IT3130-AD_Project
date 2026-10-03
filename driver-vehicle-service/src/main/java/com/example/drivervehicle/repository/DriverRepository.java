package com.example.drivervehicle.repository;

import com.example.drivervehicle.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    List<Driver> findByAvailableTrue();

    List<Driver> findByAvailableTrueAndServiceAreaIgnoreCase(String serviceArea);

    boolean existsByLicenseNumber(String licenseNumber);

    boolean existsByLicenseNumberAndIdNot(String licenseNumber, Long id);
}
