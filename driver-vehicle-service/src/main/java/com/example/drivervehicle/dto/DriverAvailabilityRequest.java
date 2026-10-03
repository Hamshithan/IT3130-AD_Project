package com.example.drivervehicle.dto;

import jakarta.validation.constraints.NotNull;

public class DriverAvailabilityRequest {

    @NotNull(message = "Availability status is required")
    private Boolean available;

    public DriverAvailabilityRequest() {
    }

    public DriverAvailabilityRequest(Boolean available) {
        this.available = available;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
