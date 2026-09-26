package com.ridelink.ridemanagement.dto;

import com.ridelink.ridemanagement.domain.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideResponse {

    private UUID rideId;
    private UUID passengerId;
    private UUID driverId;
    private LocationDto pickupLocation;
    private LocationDto destinationLocation;
    private RideStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    private String cancellationReason;
}
