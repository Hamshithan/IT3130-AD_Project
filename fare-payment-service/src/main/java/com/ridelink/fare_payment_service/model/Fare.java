package com.ridelink.fare_payment_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "fares")
public class Fare {

    @Id
    private String id;

    private String rideId;
    private double distanceKm;
    private double baseFare;
    private double perKmRate;
    private double finalFare;

    public Fare() {
    }

    public Fare(String rideId,
                double distanceKm,
                double baseFare,
                double perKmRate,
                double finalFare) {

        this.rideId = rideId;
        this.distanceKm = distanceKm;
        this.baseFare = baseFare;
        this.perKmRate = perKmRate;
        this.finalFare = finalFare;
    }

    public String getId() {
        return id;
    }

    public String getRideId() {
        return rideId;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public double getPerKmRate() {
        return perKmRate;
    }

    public double getFinalFare() {
        return finalFare;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public void setBaseFare(double baseFare) {
        this.baseFare = baseFare;
    }

    public void setPerKmRate(double perKmRate) {
        this.perKmRate = perKmRate;
    }

    public void setFinalFare(double finalFare) {
        this.finalFare = finalFare;
    }
}
