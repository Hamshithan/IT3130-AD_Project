package com.ridelink.ridemanagement.exception;

import com.ridelink.ridemanagement.domain.RideStatus;

public class InvalidRideStatusException extends RuntimeException {

    public InvalidRideStatusException(RideStatus currentStatus, RideStatus requestedStatus) {
        super(String.format("Cannot transition ride status from '%s' to '%s'", currentStatus, requestedStatus));
    }

    public InvalidRideStatusException(String message) {
        super(message);
    }
}
