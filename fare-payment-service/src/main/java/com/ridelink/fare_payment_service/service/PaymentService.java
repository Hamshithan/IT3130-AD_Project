package com.ridelink.fare_payment_service.service;

import com.ridelink.fare_payment_service.model.Payment;
import com.ridelink.fare_payment_service.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import com.ridelink.fare_payment_service.exception.ResourceNotFoundException;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment processPayment(String rideId,
                                  double amount,
                                  String paymentMethod) {

        Payment existingPayment = paymentRepository
                .findByRideId(rideId)
                .orElse(null);

        if (existingPayment != null) {
            return existingPayment;
        }

        Payment payment = new Payment(
                rideId,
                amount,
                paymentMethod,
                "SUCCESS"
        );

        return paymentRepository.save(payment);
    }

    public Payment getPaymentByRideId(String rideId) {

        return paymentRepository.findByRideId(rideId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found for ride: " + rideId
                        ));
    }

    public String getPaymentStatus(String rideId) {

        Payment payment = paymentRepository.findByRideId(rideId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found for ride: " + rideId
                        ));

        return payment.getPaymentStatus();
    }
}