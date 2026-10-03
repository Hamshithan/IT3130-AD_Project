package com.ridelink.fare_payment_service.service;

import com.ridelink.fare_payment_service.controller.ReceiptController;
import com.ridelink.fare_payment_service.dto.ReceiptRequest;
import com.ridelink.fare_payment_service.model.Receipt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceiptControllerTest {

    @Mock
    private ReceiptService receiptService;

    @InjectMocks
    private ReceiptController receiptController;

    @Test
    void generateReceipt_shouldReturnReceipt() {

        Receipt receipt = new Receipt(
                "R100",
                "PAY100",
                600.0,
                "SUCCESS"
        );

        ReceiptRequest request = new ReceiptRequest();
        request.setRideId("R100");
        request.setPaymentId("PAY100");
        request.setAmount(600.0);
        request.setPaymentStatus("SUCCESS");

        when(receiptService.generateReceipt(
                "R100",
                "PAY100",
                600.0,
                "SUCCESS"
        )).thenReturn(receipt);

        ResponseEntity<Receipt> response =
                receiptController.generateReceipt(request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("R100", response.getBody().getRideId());
        assertEquals("PAY100", response.getBody().getPaymentId());
        assertEquals(600.0, response.getBody().getAmount());
        assertEquals("SUCCESS", response.getBody().getPaymentStatus());

        verify(receiptService, times(1))
                .generateReceipt(
                        "R100",
                        "PAY100",
                        600.0,
                        "SUCCESS"
                );
    }

    @Test
    void getReceipt_shouldReturnReceipt() {

        Receipt receipt = new Receipt(
                "R100",
                "PAY100",
                600.0,
                "SUCCESS"
        );

        when(receiptService.getReceiptByRideId("R100"))
                .thenReturn(receipt);

        ResponseEntity<Receipt> response =
                receiptController.getReceipt("R100");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("R100", response.getBody().getRideId());
        assertEquals("PAY100", response.getBody().getPaymentId());
        assertEquals(600.0, response.getBody().getAmount());
        assertEquals("SUCCESS", response.getBody().getPaymentStatus());

        verify(receiptService, times(1))
                .getReceiptByRideId("R100");
    }
}
