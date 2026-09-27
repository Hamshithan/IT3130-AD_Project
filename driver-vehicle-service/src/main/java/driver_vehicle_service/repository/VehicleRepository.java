package com.example.drivervehicle.repository;

import com.example.drivervehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByDriverId(Long driverId);
}
