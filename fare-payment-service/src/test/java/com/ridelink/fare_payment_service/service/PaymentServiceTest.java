package com.ridelink.fare_payment_service.service;

import com.ridelink.fare_payment_service.model.Payment;
import com.ridelink.fare_payment_service.repository.PaymentRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void processPayment_shouldCreatePaymentSuccessfully() {

        Payment payment = new Payment(
                "R100",
                600.0,
                "CARD",
                "SUCCESS"
        );

        when(paymentRepository.findByRideId("R100"))
                .thenReturn(Optional.empty());

        when(paymentRepository.save(any(Payment.class)))
                .thenReturn(payment);

        Payment result = paymentService.processPayment(
                "R100",
                600.0,
                "CARD"
        );

        assertNotNull(result);
        assertEquals("R100", result.getRideId());
        assertEquals(600.0, result.getAmount());
        assertEquals("CARD", result.getPaymentMethod());
        assertEquals("SUCCESS", result.getPaymentStatus());

        verify(paymentRepository, times(1))
                .save(any(Payment.class));
    }

    @Test
    void processPayment_shouldReturnExistingPayment() {

        Payment existingPayment = new Payment(
                "R100",
                600.0,
                "CARD",
                "SUCCESS"
        );

        when(paymentRepository.findByRideId("R100"))
                .thenReturn(Optional.of(existingPayment));

        Payment result = paymentService.processPayment(
                "R100",
                600.0,
                "CARD"
        );

        assertNotNull(result);
        assertEquals("R100", result.getRideId());
        assertEquals(600.0, result.getAmount());
        assertEquals("SUCCESS", result.getPaymentStatus());

        verify(paymentRepository, never())
                .save(any(Payment.class));
    }

    @Test
    void getPaymentByRideId_shouldReturnPayment() {

        Payment payment = new Payment(
                "R100",
                600.0,
                "CARD",
                "SUCCESS"
        );

        when(paymentRepository.findByRideId("R100"))
                .thenReturn(Optional.of(payment));

        Payment result = paymentService.getPaymentByRideId("R100");

        assertNotNull(result);
        assertEquals("R100", result.getRideId());
        assertEquals(600.0, result.getAmount());
        assertEquals("SUCCESS", result.getPaymentStatus());

        verify(paymentRepository, times(1))
                .findByRideId("R100");
    }

    @Test
    void getPaymentStatus_shouldReturnStatus() {

        Payment payment = new Payment(
                "R100",
                600.0,
                "CARD",
                "SUCCESS"
        );

        when(paymentRepository.findByRideId("R100"))
                .thenReturn(Optional.of(payment));

        String status = paymentService.getPaymentStatus("R100");

        assertEquals("SUCCESS", status);

        verify(paymentRepository, times(1))
                .findByRideId("R100");
    }

    @Test
    void getPaymentStatus_shouldThrowExceptionWhenNotFound() {

        when(paymentRepository.findByRideId("NOTFOUND"))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> paymentService.getPaymentStatus("NOTFOUND")
        );

        verify(paymentRepository, times(1))
                .findByRideId("NOTFOUND");
    }
}
