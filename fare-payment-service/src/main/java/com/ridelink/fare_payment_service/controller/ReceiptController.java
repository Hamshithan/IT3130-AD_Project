package com.ridelink.fare_payment_service.controller;

import com.ridelink.fare_payment_service.dto.ReceiptRequest;
import com.ridelink.fare_payment_service.model.Receipt;
import com.ridelink.fare_payment_service.service.ReceiptService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PostMapping
    public ResponseEntity<Receipt> generateReceipt(
            @Valid @RequestBody ReceiptRequest request) {

        Receipt receipt = receiptService.generateReceipt(
                request.getRideId(),
                request.getPaymentId(),
                request.getAmount(),
                request.getPaymentStatus()
        );

        return ResponseEntity.ok(receipt);
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<Receipt> getReceipt(
            @PathVariable String rideId) {

        Receipt receipt = receiptService.getReceiptByRideId(rideId);

        return ResponseEntity.ok(receipt);
    }
}