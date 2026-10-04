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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private DriverVehicleClient driverVehicleClient;

    @InjectMocks
    private RideServiceImpl rideService;

    private UUID passengerId;
    private Long driverId;
    private UUID rideId;
    private LocationDto pickupDto;
    private LocationDto destDto;

    @BeforeEach
    void setUp() {
        passengerId = UUID.randomUUID();
        driverId = 42L;
        rideId = UUID.randomUUID();

        pickupDto = LocationDto.builder()
                .placeName("Negombo Bus Stand")
                .latitude(7.2083)
                .longitude(79.8358)
                .build();

        destDto = LocationDto.builder()
                .placeName("Colombo Fort")
                .latitude(6.9344)
                .longitude(79.8428)
                .build();
    }

    @Test
    @DisplayName("Should successfully create a ride request")
    void testCreateRideSuccess() {
        CreateRideRequest request = CreateRideRequest.builder()
                .passengerId(passengerId)
                .pickupLocation(pickupDto)
                .destinationLocation(destDto)
                .build();

        Ride savedRide = Ride.builder()
                .id(rideId)
                .passengerId(passengerId)
                .pickupLocation(Location.builder().placeName("Negombo Bus Stand").latitude(7.2083).longitude(79.8358).build())
                .destinationLocation(Location.builder().placeName("Colombo Fort").latitude(6.9344).longitude(79.8428).build())
                .status(RideStatus.REQUESTED)
                .build();

        when(rideRepository.save(any(Ride.class))).thenReturn(savedRide);

        RideResponse response = rideService.createRide(request);

        assertNotNull(response);
        assertEquals(rideId, response.getRideId());
        assertEquals(passengerId, response.getPassengerId());
        assertEquals(RideStatus.REQUESTED, response.getStatus());
        assertEquals("Negombo Bus Stand", response.getPickupLocation().getPlaceName());
        verify(rideRepository, times(1)).save(any(Ride.class));
    }

    @Test
    @DisplayName("Should retrieve a ride by ID")
    void testGetRideByIdSuccess() {
        Ride ride = Ride.builder()
                .id(rideId)
                .passengerId(passengerId)
                .status(RideStatus.REQUESTED)
                .build();

        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

        RideResponse response = rideService.getRideById(rideId);

        assertNotNull(response);
        assertEquals(rideId, response.getRideId());
        assertEquals(RideStatus.REQUESTED, response.getStatus());
    }

    @Test
    @DisplayName("Should throw RideNotFoundException when ride does not exist")
    void testGetRideByIdNotFound() {
        when(rideRepository.findById(rideId)).thenReturn(Optional.empty());

        assertThrows(RideNotFoundException.class, () -> rideService.getRideById(rideId));
    }

    @Test
    @DisplayName("Should successfully assign an available driver to a REQUESTED ride")
    void testAssignDriverSuccess() {
        Ride ride = Ride.builder()
                .id(rideId)
                .passengerId(passengerId)
                .status(RideStatus.REQUESTED)
                .build();

        AssignDriverRequest request = new AssignDriverRequest(driverId);

        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(driverVehicleClient.isDriverAvailable(driverId)).thenReturn(true);
        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RideResponse response = rideService.assignDriver(rideId, request);

        assertEquals(RideStatus.ASSIGNED, response.getStatus());
        assertEquals(driverId, response.getDriverId());
    }

    @Test
    @DisplayName("Should throw InvalidRideStatusException when assigned driver is unavailable")
    void testAssignDriverUnavailable() {
        Ride ride = Ride.builder()
                .id(rideId)
                .passengerId(passengerId)
                .status(RideStatus.REQUESTED)
                .build();

        AssignDriverRequest request = new AssignDriverRequest(driverId);

        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(driverVehicleClient.isDriverAvailable(driverId)).thenReturn(false);

        assertThrows(InvalidRideStatusException.class, () -> rideService.assignDriver(rideId, request));
    }

    @Test
    @DisplayName("Should execute valid lifecycle transition: REQUESTED -> ASSIGNED -> ACCEPTED -> IN_PROGRESS -> COMPLETED")
    void testValidLifecycleTransitions() {
        Ride ride = Ride.builder()
                .id(rideId)
                .passengerId(passengerId)
                .driverId(driverId)
                .status(RideStatus.ACCEPTED)
                .build();

        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Start ride
        RideResponse inProgressResponse = rideService.startRide(rideId);
        assertEquals(RideStatus.IN_PROGRESS, inProgressResponse.getStatus());

        // Complete ride
        RideResponse completedResponse = rideService.completeRide(rideId);
        assertEquals(RideStatus.COMPLETED, completedResponse.getStatus());
        assertNotNull(completedResponse.getCompletedAt());
    }

    @Test
    @DisplayName("Should throw InvalidRideStatusException (409 Conflict) for invalid status transition (e.g. Starting COMPLETED ride)")
    void testInvalidStatusTransition() {
        Ride completedRide = Ride.builder()
                .id(rideId)
                .passengerId(passengerId)
                .driverId(driverId)
                .status(RideStatus.COMPLETED)
                .build();

        when(rideRepository.findById(rideId)).thenReturn(Optional.of(completedRide));

        assertThrows(InvalidRideStatusException.class, () -> rideService.startRide(rideId));
    }

    @Test
    @DisplayName("Should cancel a ride with reason")
    void testCancelRideSuccess() {
        Ride ride = Ride.builder()
                .id(rideId)
                .passengerId(passengerId)
                .status(RideStatus.REQUESTED)
                .build();

        CancelRideRequest cancelRequest = new CancelRideRequest("Passenger plans changed");

        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RideResponse response = rideService.cancelRide(rideId, cancelRequest);

        assertEquals(RideStatus.CANCELLED, response.getStatus());
        assertEquals("Passenger plans changed", response.getCancellationReason());
    }

    @Test
    @DisplayName("Should retrieve rides by passenger ID")
    void testGetRidesByPassengerId() {
        Ride ride = Ride.builder().id(rideId).passengerId(passengerId).status(RideStatus.REQUESTED).build();
        when(rideRepository.findByPassengerId(passengerId)).thenReturn(List.of(ride));

        List<RideResponse> responses = rideService.getRidesByPassengerId(passengerId);

        assertEquals(1, responses.size());
        assertEquals(passengerId, responses.get(0).getPassengerId());
    }
}
