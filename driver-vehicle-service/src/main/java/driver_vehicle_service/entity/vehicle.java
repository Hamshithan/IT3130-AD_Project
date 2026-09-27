package com.example.drivervehicle.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long driverId;

    private String vehicleType;

    private String brand;

    private String model;

    private String registrationNumber;

    private String color;

    public Vehicle() {
    }

    public Vehicle(Long driverId,
                   String vehicleType,
                   String brand,
                   String model,
                   String registrationNumber,
                   String color) {

        this.driverId = driverId;
        this.vehicleType = vehicleType;
        this.brand = brand;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public Long getDriverId() {
        return driverId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getColor() {
        return color;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setColor(String color) {
        this.color = color;
    }
}