package com.ridelink.ridemanagement.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Minimal projection of the Driver-Vehicle Service's DriverResponse.
 * Only the fields required by ride-management-service are mapped here.
 * Unknown fields from the remote response are safely ignored via @JsonIgnoreProperties.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverAvailabilityResponse {

    private Long id;
    private boolean available;

    public DriverAvailabilityResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
