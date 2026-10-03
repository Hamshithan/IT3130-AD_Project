package com.example.drivervehicle.dto;

import com.example.drivervehicle.entity.Vehicle;

public class VehicleResponse {

    private Long id;
    private Long driverId;
    private String vehicleType;
    private String brand;
    private String model;
    private String registrationNumber;
    private String color;

    public VehicleResponse() {
    }

    public VehicleResponse(Long id, Long driverId, String vehicleType, String brand, String model, String registrationNumber, String color) {
        this.id = id;
        this.driverId = driverId;
        this.vehicleType = vehicleType;
        this.brand = brand;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.color = color;
    }

    public static VehicleResponse fromEntity(Vehicle vehicle) {
        if (vehicle == null) return null;
        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getDriverId(),
                vehicle.getVehicleType(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getRegistrationNumber(),
                vehicle.getColor()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
