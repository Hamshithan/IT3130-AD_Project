package com.ridelink.ridemanagement.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRideRequest {

    @NotNull(message = "Passenger ID is required")
    private UUID passengerId;

    @NotNull(message = "Pickup location is required")
    @Valid
    private LocationDto pickupLocation;

    @NotNull(message = "Destination location is required")
    @Valid
    private LocationDto destinationLocation;
}
