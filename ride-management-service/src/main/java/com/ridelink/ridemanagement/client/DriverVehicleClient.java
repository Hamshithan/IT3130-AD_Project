package com.ridelink.ridemanagement.client;

/**
 * Interface for interservice communication with Driver & Vehicle Service.
 * Note: Actual HTTP client integration (REST/Feign) to be completed once the Driver & Vehicle Service API contract is deployed.
 */
public interface DriverVehicleClient {

    boolean isDriverAvailable(Long driverId);
}
