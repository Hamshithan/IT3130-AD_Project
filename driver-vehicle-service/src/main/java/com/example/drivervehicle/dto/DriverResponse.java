package com.example.drivervehicle.dto;

import com.example.drivervehicle.entity.Driver;

public class DriverResponse {

    private Long id;
    private String name;
    private String phone;
    private String licenseNumber;
    private boolean available;
    private String serviceArea;
    private Double latitude;
    private Double longitude;

    public DriverResponse() {
    }

    public DriverResponse(Long id, String name, String phone, String licenseNumber, boolean available, String serviceArea, Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
        this.available = available;
        this.serviceArea = serviceArea;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static DriverResponse fromEntity(Driver driver) {
        if (driver == null) return null;
        return new DriverResponse(
                driver.getId(),
                driver.getName(),
                driver.getPhone(),
                driver.getLicenseNumber(),
                driver.isAvailable(),
                driver.getServiceArea(),
                driver.getLatitude(),
                driver.getLongitude()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
