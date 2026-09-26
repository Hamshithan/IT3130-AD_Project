package com.ridelink.ridemanagement.client;

import com.ridelink.ridemanagement.dto.LocationDto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Interface for interservice communication with Fare & Payment Service.
 * Note: Actual HTTP client integration to be completed once the Fare & Payment Service API contract is deployed.
 */
public interface FarePaymentClient {

    BigDecimal calculateEstimatedFare(LocationDto pickup, LocationDto destination);

    void notifyRideCompletion(UUID rideId, UUID passengerId, UUID driverId, BigDecimal fareAmount);
}
