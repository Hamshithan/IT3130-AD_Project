package com.ridelink.ridemanagement.client;

import com.ridelink.ridemanagement.dto.LocationDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Temporary mock implementation of FarePaymentClient for local development and testing.
 * Will be replaced by actual REST/WebClient implementation once Fare & Payment Service is live.
 */
@Component
public class MockFarePaymentClient implements FarePaymentClient {

    @Override
    public BigDecimal calculateEstimatedFare(LocationDto pickup, LocationDto destination) {
        // Temporary stub returning standard mock estimate
        return BigDecimal.valueOf(15.00);
    }

    @Override
    public void notifyRideCompletion(UUID rideId, UUID passengerId, UUID driverId, BigDecimal fareAmount) {
        // Temporary stub log notification
    }
}
