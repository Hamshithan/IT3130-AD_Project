package com.example.drivervehicle.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    private String licenseNumber;

    private boolean available;

    private String serviceArea;

    private Double latitude;

    private Double longitude;

    public Driver() {
    }

    public Driver(String name,
                  String phone,
                  String licenseNumber,
                  boolean available,
                  String serviceArea,
                  Double latitude,
                  Double longitude) {

        this.name = name;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
        this.available = available;
        this.serviceArea = serviceArea;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}