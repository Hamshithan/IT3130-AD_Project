package com.ridelink.fare_payment_service.service;

import com.ridelink.fare_payment_service.model.Receipt;
import com.ridelink.fare_payment_service.repository.ReceiptRepository;
import org.springframework.stereotype.Service;
import com.ridelink.fare_payment_service.exception.ResourceNotFoundException;

@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    public ReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    public Receipt generateReceipt(String rideId,
                                   String paymentId,
                                   double amount,
                                   String paymentStatus) {

        Receipt existingReceipt = receiptRepository
                .findByRideId(rideId)
                .orElse(null);

        if (existingReceipt != null) {
            return existingReceipt;
        }

        Receipt receipt = new Receipt(
                rideId,
                paymentId,
                amount,
                paymentStatus
        );

        return receiptRepository.save(receipt);
    }

    public Receipt getReceiptByRideId(String rideId) {

        return receiptRepository.findByRideId(rideId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Receipt not found for ride: " + rideId
                        ));
}

}
