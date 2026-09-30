package com.ridelink.fare_payment_service.service;

import com.ridelink.fare_payment_service.controller.PaymentController;
import com.ridelink.fare_payment_service.dto.PaymentRequest;
import com.ridelink.fare_payment_service.model.Payment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    void processPayment_shouldReturnPayment() {

        Payment payment = new Payment(
                "R100",
                600.0,
                "CARD",
                "SUCCESS"
        );

        PaymentRequest request = new PaymentRequest();
        request.setRideId("R100");
        request.setAmount(600.0);
        request.setPaymentMethod("CARD");

        when(paymentService.processPayment(
                "R100",
                600.0,
                "CARD"
        )).thenReturn(payment);

        ResponseEntity<Payment> response =
                paymentController.processPayment(request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("R100", response.getBody().getRideId());
        assertEquals(600.0, response.getBody().getAmount());
        assertEquals("CARD", response.getBody().getPaymentMethod());
        assertEquals("SUCCESS", response.getBody().getPaymentStatus());

        verify(paymentService, times(1))
                .processPayment("R100", 600.0, "CARD");
    }

    @Test
    void getPayment_shouldReturnPayment() {

        Payment payment = new Payment(
                "R100",
                600.0,
                "CARD",
                "SUCCESS"
        );

        when(paymentService.getPaymentByRideId("R100"))
                .thenReturn(payment);

        ResponseEntity<Payment> response =
                paymentController.getPayment("R100");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("R100", response.getBody().getRideId());
        assertEquals(600.0, response.getBody().getAmount());

        verify(paymentService, times(1))
                .getPaymentByRideId("R100");
    }

    @Test
    void getPaymentStatus_shouldReturnStatus() {

        when(paymentService.getPaymentStatus("R100"))
                .thenReturn("SUCCESS");

        ResponseEntity<String> response =
                paymentController.getPaymentStatus("R100");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("SUCCESS", response.getBody());

        verify(paymentService, times(1))
                .getPaymentStatus("R100");
    }
}
