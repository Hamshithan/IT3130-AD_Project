package com.ridelink.fare_payment_service.service;

import com.ridelink.fare_payment_service.controller.FareController;
import com.ridelink.fare_payment_service.model.Fare;
import com.ridelink.fare_payment_service.dto.FareRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FareControllerTest {

    @Mock
    private FareService fareService;

    @InjectMocks
    private FareController fareController;

    @Test
    void calculateFare_shouldReturnFare() {

        Fare fare = new Fare(
                "R100",
                10,
                100.0,
                50.0,
                600.0
        );

        FareRequest request = new FareRequest();
        request.setRideId("R100");
        request.setDistanceKm(10);

        when(fareService.calculateFare("R100", 10))
                .thenReturn(fare);

        ResponseEntity<Fare> response =
                fareController.calculateFare(request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("R100", response.getBody().getRideId());
        assertEquals(600.0, response.getBody().getFinalFare());

        verify(fareService, times(1))
                .calculateFare("R100", 10);
    }

    @Test
    void getFare_shouldReturnFare() {

        Fare fare = new Fare(
                "R100",
                10,
                100.0,
                50.0,
                600.0
        );

        when(fareService.getFareByRideId("R100"))
                .thenReturn(fare);

        ResponseEntity<Fare> response =
                fareController.getFare("R100");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("R100", response.getBody().getRideId());
        assertEquals(600.0, response.getBody().getFinalFare());

        verify(fareService, times(1))
                .getFareByRideId("R100");
    }
}
