package com.ridelink.fare_payment_service.service;

import com.ridelink.fare_payment_service.model.Receipt;
import com.ridelink.fare_payment_service.repository.ReceiptRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {

    @Mock
    private ReceiptRepository receiptRepository;

    @InjectMocks
    private ReceiptService receiptService;

    @Test
    void generateReceipt_shouldCreateReceiptSuccessfully() {

        Receipt receipt = new Receipt(
                "R100",
                "PAY100",
                600.0,
                "SUCCESS"
        );

        when(receiptRepository.findByRideId("R100"))
                .thenReturn(Optional.empty());

        when(receiptRepository.save(any(Receipt.class)))
                .thenReturn(receipt);

        Receipt result = receiptService.generateReceipt(
                "R100",
                "PAY100",
                600.0,
                "SUCCESS"
        );

        assertNotNull(result);
        assertEquals("R100", result.getRideId());
        assertEquals("PAY100", result.getPaymentId());
        assertEquals(600.0, result.getAmount());
        assertEquals("SUCCESS", result.getPaymentStatus());

        verify(receiptRepository, times(1))
                .save(any(Receipt.class));
    }

    @Test
    void generateReceipt_shouldReturnExistingReceipt() {

        Receipt existingReceipt = new Receipt(
                "R100",
                "PAY100",
                600.0,
                "SUCCESS"
        );

        when(receiptRepository.findByRideId("R100"))
                .thenReturn(Optional.of(existingReceipt));

        Receipt result = receiptService.generateReceipt(
                "R100",
                "PAY100",
                600.0,
                "SUCCESS"
        );

        assertNotNull(result);
        assertEquals("R100", result.getRideId());
        assertEquals("PAY100", result.getPaymentId());
        assertEquals(600.0, result.getAmount());
        assertEquals("SUCCESS", result.getPaymentStatus());

        verify(receiptRepository, never())
                .save(any(Receipt.class));
    }

    @Test
    void getReceiptByRideId_shouldReturnReceipt() {

        Receipt receipt = new Receipt(
                "R100",
                "PAY100",
                600.0,
                "SUCCESS"
        );

        when(receiptRepository.findByRideId("R100"))
                .thenReturn(Optional.of(receipt));

        Receipt result = receiptService.getReceiptByRideId("R100");

        assertNotNull(result);
        assertEquals("R100", result.getRideId());
        assertEquals("PAY100", result.getPaymentId());
        assertEquals(600.0, result.getAmount());
        assertEquals("SUCCESS", result.getPaymentStatus());

        verify(receiptRepository, times(1))
                .findByRideId("R100");
    }

    @Test
    void getReceiptByRideId_shouldThrowExceptionWhenNotFound() {

        when(receiptRepository.findByRideId("NOTFOUND"))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> receiptService.getReceiptByRideId("NOTFOUND")
        );

        verify(receiptRepository, times(1))
                .findByRideId("NOTFOUND");
    }
}
