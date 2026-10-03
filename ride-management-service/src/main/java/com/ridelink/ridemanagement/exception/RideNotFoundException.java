package com.ridelink.ridemanagement.exception;

import java.util.UUID;

public class RideNotFoundException extends RuntimeException {

    public RideNotFoundException(UUID rideId) {
        super("Ride not found with ID: " + rideId);
    }
}
