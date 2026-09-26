package com.ridelink.ridemanagement.exception;

public class InterserviceException extends RuntimeException {

    public InterserviceException(String serviceName, String message) {
        super(String.format("Integration failure with service '%s': %s", serviceName, message));
    }
}
