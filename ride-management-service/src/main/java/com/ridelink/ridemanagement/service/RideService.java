package com.ridelink.ridemanagement.service;

import com.ridelink.ridemanagement.dto.AssignDriverRequest;
import com.ridelink.ridemanagement.dto.CancelRideRequest;
import com.ridelink.ridemanagement.dto.CreateRideRequest;
import com.ridelink.ridemanagement.dto.RideResponse;

import java.util.List;
import java.util.UUID;

public interface RideService {

    RideResponse createRide(CreateRideRequest request);

    RideResponse getRideById(UUID rideId);

    List<RideResponse> getRidesByPassengerId(UUID passengerId);

    List<RideResponse> getRidesByDriverId(Long driverId);

    RideResponse assignDriver(UUID rideId, AssignDriverRequest request);

    RideResponse acceptRide(UUID rideId);

    RideResponse startRide(UUID rideId);

    RideResponse completeRide(UUID rideId);

    RideResponse cancelRide(UUID rideId, CancelRideRequest request);
}
