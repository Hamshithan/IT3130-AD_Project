package com.ridelink.fare_payment_service.repository;

import com.ridelink.fare_payment_service.model.Receipt;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReceiptRepository
        extends MongoRepository<Receipt, String> {

    Optional<Receipt> findByRideId(String rideId);
}
