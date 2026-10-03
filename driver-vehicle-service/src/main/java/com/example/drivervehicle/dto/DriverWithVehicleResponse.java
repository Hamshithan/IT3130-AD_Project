package com.example.drivervehicle.dto;

import com.example.drivervehicle.entity.Driver;
import com.example.drivervehicle.entity.Vehicle;

public class DriverWithVehicleResponse {

    private DriverResponse driver;
    private VehicleResponse vehicle;

    public DriverWithVehicleResponse() {
    }

    public DriverWithVehicleResponse(DriverResponse driver, VehicleResponse vehicle) {
        this.driver = driver;
        this.vehicle = vehicle;
    }

    public static DriverWithVehicleResponse of(Driver driver, Vehicle vehicle) {
        return new DriverWithVehicleResponse(
                DriverResponse.fromEntity(driver),
                VehicleResponse.fromEntity(vehicle)
        );
    }

    public DriverResponse getDriver() {
        return driver;
    }

    public void setDriver(DriverResponse driver) {
        this.driver = driver;
    }

    public VehicleResponse getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleResponse vehicle) {
        this.vehicle = vehicle;
    }
}
