package com.example.drivervehicle.service;

import com.example.drivervehicle.dto.VehicleCreateRequest;
import com.example.drivervehicle.dto.VehicleResponse;
import com.example.drivervehicle.dto.VehicleUpdateRequest;
import com.example.drivervehicle.entity.Vehicle;
import com.example.drivervehicle.exception.DuplicateResourceException;
import com.example.drivervehicle.exception.ResourceNotFoundException;
import com.example.drivervehicle.repository.DriverRepository;
import com.example.drivervehicle.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    public VehicleService(VehicleRepository vehicleRepository, DriverRepository driverRepository) {
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
    }

    public Vehicle createVehicle(Vehicle vehicle) {
        if (vehicle.getDriverId() != null && !driverRepository.existsById(vehicle.getDriverId())) {
            throw new ResourceNotFoundException("Driver not found with id: " + vehicle.getDriverId());
        }
        if (vehicle.getRegistrationNumber() != null && vehicleRepository.existsByRegistrationNumber(vehicle.getRegistrationNumber())) {
            throw new DuplicateResourceException("Vehicle with registration number '" + vehicle.getRegistrationNumber() + "' already exists");
        }
        return vehicleRepository.save(vehicle);
    }

    public VehicleResponse createVehicle(VehicleCreateRequest request) {
        if (!driverRepository.existsById(request.getDriverId())) {
            throw new ResourceNotFoundException("Driver not found with id: " + request.getDriverId());
        }
        if (vehicleRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new DuplicateResourceException("Vehicle with registration number '" + request.getRegistrationNumber() + "' already exists");
        }
        Vehicle vehicle = new Vehicle(
                request.getDriverId(),
                request.getVehicleType(),
                request.getBrand(),
                request.getModel(),
                request.getRegistrationNumber(),
                request.getColor()
        );
        Vehicle saved = vehicleRepository.save(vehicle);
        return VehicleResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<VehicleResponse> getAllVehicleResponses() {
        return vehicleRepository.findAll().stream()
                .map(VehicleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
    }

    @Transactional(readOnly = true)
    public VehicleResponse getVehicleResponseById(Long id) {
        Vehicle vehicle = getVehicleById(id);
        return VehicleResponse.fromEntity(vehicle);
    }

    @Transactional(readOnly = true)
    public Vehicle getVehicleByDriverId(Long driverId) {
        return vehicleRepository.findByDriverId(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found for driver"));
    }

    @Transactional(readOnly = true)
    public VehicleResponse getVehicleResponseByDriverId(Long driverId) {
        Vehicle vehicle = getVehicleByDriverId(driverId);
        return VehicleResponse.fromEntity(vehicle);
    }

    public Vehicle updateVehicle(Long id, Vehicle updatedVehicle) {
        Vehicle vehicle = getVehicleById(id);

        if (updatedVehicle.getDriverId() != null && !driverRepository.existsById(updatedVehicle.getDriverId())) {
            throw new ResourceNotFoundException("Driver not found with id: " + updatedVehicle.getDriverId());
        }
        if (updatedVehicle.getRegistrationNumber() != null &&
                vehicleRepository.existsByRegistrationNumberAndIdNot(updatedVehicle.getRegistrationNumber(), id)) {
            throw new DuplicateResourceException("Vehicle with registration number '" + updatedVehicle.getRegistrationNumber() + "' already exists");
        }

        vehicle.setDriverId(updatedVehicle.getDriverId());
        vehicle.setVehicleType(updatedVehicle.getVehicleType());
        vehicle.setBrand(updatedVehicle.getBrand());
        vehicle.setModel(updatedVehicle.getModel());
        vehicle.setRegistrationNumber(updatedVehicle.getRegistrationNumber());
        vehicle.setColor(updatedVehicle.getColor());

        return vehicleRepository.save(vehicle);
    }

    public VehicleResponse updateVehicle(Long id, VehicleUpdateRequest request) {
        Vehicle vehicle = getVehicleById(id);

        if (!driverRepository.existsById(request.getDriverId())) {
            throw new ResourceNotFoundException("Driver not found with id: " + request.getDriverId());
        }
        if (vehicleRepository.existsByRegistrationNumberAndIdNot(request.getRegistrationNumber(), id)) {
            throw new DuplicateResourceException("Vehicle with registration number '" + request.getRegistrationNumber() + "' already exists");
        }

        vehicle.setDriverId(request.getDriverId());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setRegistrationNumber(request.getRegistrationNumber());
        vehicle.setColor(request.getColor());

        Vehicle saved = vehicleRepository.save(vehicle);
        return VehicleResponse.fromEntity(saved);
    }

    public void deleteVehicle(Long id) {
        Vehicle vehicle = getVehicleById(id);
        vehicleRepository.delete(vehicle);
    }
}