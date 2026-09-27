package com.example.drivervehicle.service;

import com.example.drivervehicle.entity.Vehicle;
import com.example.drivervehicle.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(Long id) {

        return vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Vehicle not found"));
    }

    public Vehicle getVehicleByDriverId(Long driverId) {

        return vehicleRepository.findByDriverId(driverId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Vehicle not found for driver"));
    }

    public Vehicle updateVehicle(Long id, Vehicle updatedVehicle) {

        Vehicle vehicle = getVehicleById(id);

        vehicle.setDriverId(updatedVehicle.getDriverId());
        vehicle.setVehicleType(updatedVehicle.getVehicleType());
        vehicle.setBrand(updatedVehicle.getBrand());
        vehicle.setModel(updatedVehicle.getModel());
        vehicle.setRegistrationNumber(
                updatedVehicle.getRegistrationNumber()
        );
        vehicle.setColor(updatedVehicle.getColor());

        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }
}