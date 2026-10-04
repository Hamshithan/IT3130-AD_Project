package com.ridelink.ridemanagement.client;

/**
 * Temporary mock implementation of DriverVehicleClient for unit testing.
 * It is not registered as a Spring bean because the real HTTP implementation
 * is used by the running application.
 */
public class MockDriverVehicleClient implements DriverVehicleClient {

    @Override
    public boolean isDriverAvailable(Long driverId) {
        // Mock behavior for unit tests
        return driverId != null;
    }
}