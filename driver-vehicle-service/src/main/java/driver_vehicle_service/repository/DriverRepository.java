package com.example.drivervehicle.repository;

import com.example.drivervehicle.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    List<Driver> findByAvailableTrue();

    List<Driver> findByAvailableTrueAndServiceAreaIgnoreCase(String serviceArea);
}
