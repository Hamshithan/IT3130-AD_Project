package com.ridelink.ridemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridelink.ridemanagement.domain.RideStatus;
import com.ridelink.ridemanagement.dto.CreateRideRequest;
import com.ridelink.ridemanagement.dto.LocationDto;
import com.ridelink.ridemanagement.dto.RideResponse;
import com.ridelink.ridemanagement.exception.InvalidRideStatusException;
import com.ridelink.ridemanagement.exception.RideNotFoundException;
import com.ridelink.ridemanagement.service.RideService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RideController.class)
class RideControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RideService rideService;

    private UUID passengerId;
    private UUID rideId;
    private LocationDto pickupDto;
    private LocationDto destDto;

    @BeforeEach
    void setUp() {
        passengerId = UUID.randomUUID();
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
    @DisplayName("POST /api/v1/rides - Should return 201 Created when request is valid")
    void testCreateRideValid() throws Exception {
        CreateRideRequest request = CreateRideRequest.builder()
                .passengerId(passengerId)
                .pickupLocation(pickupDto)
                .destinationLocation(destDto)
                .build();

        RideResponse response = RideResponse.builder()
                .rideId(rideId)
                .passengerId(passengerId)
                .pickupLocation(pickupDto)
                .destinationLocation(destDto)
                .status(RideStatus.REQUESTED)
                .build();

        when(rideService.createRide(any(CreateRideRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rideId").value(rideId.toString()))
                .andExpect(jsonPath("$.status").value("REQUESTED"));
    }

    @Test
    @DisplayName("POST /api/v1/rides - Should return 400 Bad Request when validation fails")
    void testCreateRideInvalidPayload() throws Exception {
        CreateRideRequest request = CreateRideRequest.builder()
                .passengerId(null) // Missing required passengerId
                .pickupLocation(pickupDto)
                .destinationLocation(destDto)
                .build();

        mockMvc.perform(post("/api/v1/rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("GET /api/v1/rides/{rideId} - Should return 404 Not Found when ride does not exist")
    void testGetRideNotFound() throws Exception {
        when(rideService.getRideById(rideId)).thenThrow(new RideNotFoundException(rideId));

        mockMvc.perform(get("/api/v1/rides/{rideId}", rideId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    @DisplayName("PATCH /api/v1/rides/{rideId}/start - Should return 409 Conflict on invalid state transition")
    void testInvalidStatusTransitionConflict() throws Exception {
        when(rideService.startRide(rideId))
                .thenThrow(new InvalidRideStatusException(RideStatus.COMPLETED, RideStatus.IN_PROGRESS));

        mockMvc.perform(patch("/api/v1/rides/{rideId}/start", rideId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"));
    }
}
