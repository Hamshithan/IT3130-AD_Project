package com.ridelink.fare_payment_service.service;

import com.ridelink.fare_payment_service.exception.ResourceNotFoundException;
import com.ridelink.fare_payment_service.model.Fare;
import com.ridelink.fare_payment_service.repository.FareRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FareServiceTest {

    @Mock
    private FareRepository fareRepository;

    @InjectMocks
    private FareService fareService;

    @Test
    void calculateFare_shouldCalculateCorrectly() {

        Fare fare = new Fare(
                "R100",
                10,
                100.0,
                50.0,
                600.0
        );

        when(fareRepository.save(any(Fare.class)))
                .thenReturn(fare);

        Fare result = fareService.calculateFare("R100", 10);

        assertNotNull(result);
        assertEquals("R100", result.getRideId());
        assertEquals(10, result.getDistanceKm());
        assertEquals(100.0, result.getBaseFare());
        assertEquals(50.0, result.getPerKmRate());
        assertEquals(600.0, result.getFinalFare());

        verify(fareRepository, times(1))
                .save(any(Fare.class));
    }

    @Test
    void getFareByRideId_shouldReturnFare() {

        Fare fare = new Fare(
                "R100",
                10,
                100.0,
                50.0,
                600.0
        );

        when(fareRepository.findByRideId("R100"))
                .thenReturn(Optional.of(fare));

        Fare result = fareService.getFareByRideId("R100");

        assertNotNull(result);
        assertEquals("R100", result.getRideId());
        assertEquals(600.0, result.getFinalFare());

        verify(fareRepository, times(1))
                .findByRideId("R100");
    }

    @Test
    void getFareByRideId_shouldThrowExceptionWhenNotFound() {

        when(fareRepository.findByRideId("NOTFOUND"))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> fareService.getFareByRideId("NOTFOUND")
        );

        verify(fareRepository, times(1))
                .findByRideId("NOTFOUND");
    }
}