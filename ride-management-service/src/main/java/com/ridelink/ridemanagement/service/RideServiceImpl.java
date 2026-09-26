package com.ridelink.ridemanagement.service;

import com.ridelink.ridemanagement.client.DriverVehicleClient;
import com.ridelink.ridemanagement.domain.Location;
import com.ridelink.ridemanagement.domain.Ride;
import com.ridelink.ridemanagement.domain.RideStatus;
import com.ridelink.ridemanagement.dto.AssignDriverRequest;
import com.ridelink.ridemanagement.dto.CancelRideRequest;
import com.ridelink.ridemanagement.dto.CreateRideRequest;
import com.ridelink.ridemanagement.dto.LocationDto;
import com.ridelink.ridemanagement.dto.RideResponse;
import com.ridelink.ridemanagement.exception.InvalidRideStatusException;
import com.ridelink.ridemanagement.exception.RideNotFoundException;
import com.ridelink.ridemanagement.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final DriverVehicleClient driverVehicleClient;

    @Override
    @Transactional
    public RideResponse createRide(CreateRideRequest request) {
        Location pickup = mapToLocation(request.getPickupLocation());
        Location destination = mapToLocation(request.getDestinationLocation());

        Ride ride = Ride.builder()
                .passengerId(request.getPassengerId())
                .pickupLocation(pickup)
                .destinationLocation(destination)
                .status(RideStatus.REQUESTED)
                .build();

        Ride savedRide = rideRepository.save(ride);
        return mapToRideResponse(savedRide);
    }

    @Override
    @Transactional(readOnly = true)
    public RideResponse getRideById(UUID rideId) {
        Ride ride = findRideOrThrow(rideId);
        return mapToRideResponse(ride);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RideResponse> getRidesByPassengerId(UUID passengerId) {
        return rideRepository.findByPassengerId(passengerId).stream()
                .map(this::mapToRideResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RideResponse> getRidesByDriverId(UUID driverId) {
        return rideRepository.findByDriverId(driverId).stream()
                .map(this::mapToRideResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RideResponse assignDriver(UUID rideId, AssignDriverRequest request) {
        Ride ride = findRideOrThrow(rideId);
        validateStatusTransition(ride.getStatus(), RideStatus.ASSIGNED);

        if (!driverVehicleClient.isDriverAvailable(request.getDriverId())) {
            throw new InvalidRideStatusException("Driver is not available for assignment");
        }

        ride.setDriverId(request.getDriverId());
        ride.setStatus(RideStatus.ASSIGNED);
        return mapToRideResponse(rideRepository.save(ride));
    }

    @Override
    @Transactional
    public RideResponse acceptRide(UUID rideId) {
        Ride ride = findRideOrThrow(rideId);
        if (ride.getDriverId() == null) {
            throw new InvalidRideStatusException("Cannot accept a ride without an assigned driver");
        }
        validateStatusTransition(ride.getStatus(), RideStatus.ACCEPTED);

        ride.setStatus(RideStatus.ACCEPTED);
        return mapToRideResponse(rideRepository.save(ride));
    }

    @Override
    @Transactional
    public RideResponse startRide(UUID rideId) {
        Ride ride = findRideOrThrow(rideId);
        validateStatusTransition(ride.getStatus(), RideStatus.IN_PROGRESS);

        ride.setStatus(RideStatus.IN_PROGRESS);
        return mapToRideResponse(rideRepository.save(ride));
    }

    @Override
    @Transactional
    public RideResponse completeRide(UUID rideId) {
        Ride ride = findRideOrThrow(rideId);
        validateStatusTransition(ride.getStatus(), RideStatus.COMPLETED);

        ride.setStatus(RideStatus.COMPLETED);
        ride.setCompletedAt(LocalDateTime.now());
        return mapToRideResponse(rideRepository.save(ride));
    }

    @Override
    @Transactional
    public RideResponse cancelRide(UUID rideId, CancelRideRequest request) {
        Ride ride = findRideOrThrow(rideId);
        validateStatusTransition(ride.getStatus(), RideStatus.CANCELLED);

        ride.setStatus(RideStatus.CANCELLED);
        if (request != null && request.getCancellationReason() != null) {
            ride.setCancellationReason(request.getCancellationReason());
        }
        return mapToRideResponse(rideRepository.save(ride));
    }

    private Ride findRideOrThrow(UUID rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException(rideId));
    }

    private void validateStatusTransition(RideStatus currentStatus, RideStatus nextStatus) {
        if (!currentStatus.canTransitionTo(nextStatus)) {
            throw new InvalidRideStatusException(currentStatus, nextStatus);
        }
    }

    private Location mapToLocation(LocationDto dto) {
        if (dto == null) {
            return null;
        }
        return Location.builder()
                .placeName(dto.getPlaceName())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }

    private LocationDto mapToLocationDto(Location location) {
        if (location == null) {
            return null;
        }
        return LocationDto.builder()
                .placeName(location.getPlaceName())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }

    private RideResponse mapToRideResponse(Ride ride) {
        return RideResponse.builder()
                .rideId(ride.getId())
                .passengerId(ride.getPassengerId())
                .driverId(ride.getDriverId())
                .pickupLocation(mapToLocationDto(ride.getPickupLocation()))
                .destinationLocation(mapToLocationDto(ride.getDestinationLocation()))
                .status(ride.getStatus())
                .createdAt(ride.getCreatedAt())
                .updatedAt(ride.getUpdatedAt())
                .completedAt(ride.getCompletedAt())
                .cancellationReason(ride.getCancellationReason())
                .build();
    }
}
