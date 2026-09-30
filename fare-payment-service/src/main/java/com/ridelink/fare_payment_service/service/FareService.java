package com.ridelink.fare_payment_service.service;

import com.ridelink.fare_payment_service.model.Fare;
import com.ridelink.fare_payment_service.repository.FareRepository;
import org.springframework.stereotype.Service;
import com.ridelink.fare_payment_service.exception.ResourceNotFoundException;

@Service
public class FareService {

    private static final double BASE_FARE = 100.0;
    private static final double PER_KM_RATE = 50.0;

    private final FareRepository fareRepository;

    public FareService(FareRepository fareRepository) {
        this.fareRepository = fareRepository;
    }

    public Fare calculateFare(String rideId, double distanceKm) {

        double finalFare =
                BASE_FARE + (distanceKm * PER_KM_RATE);

        Fare fare = new Fare(
                rideId,
                distanceKm,
                BASE_FARE,
                PER_KM_RATE,
                finalFare
        );

        return fareRepository.save(fare);
    }

    public Fare getFareByRideId(String rideId) {

        return fareRepository.findByRideId(rideId)
              .orElseThrow(() ->
        new ResourceNotFoundException(
                "Fare not found for ride: " + rideId
        )
);  
    }
}
