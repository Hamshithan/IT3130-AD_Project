package com.ridelink.ridemanagement.client;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Temporary mock implementation of DriverVehicleClient for local development and testing.
 * Will be replaced by actual REST/WebClient implementation once Driver & Vehicle Service is live.
 */
@Component
public class MockDriverVehicleClient implements DriverVehicleClient {

    @Override
    public boolean isDriverAvailable(UUID driverId) {
        // Temporary stub: Assume all valid non-null driver UUIDs are eligible for local demo
        return driverId != null;
    }
}
