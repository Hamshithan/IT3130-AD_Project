package com.example.drivervehicle.controller;

import com.example.drivervehicle.dto.VehicleCreateRequest;
import com.example.drivervehicle.dto.VehicleResponse;
import com.example.drivervehicle.exception.GlobalExceptionHandler;
import com.example.drivervehicle.service.VehicleService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private VehicleController vehicleController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/vehicles - Register Vehicle")
    void createVehicle() throws Exception {
        VehicleCreateRequest request = new VehicleCreateRequest(1L, "SEDAN", "Toyota", "Camry", "REG-888", "Blue");
        VehicleResponse response = new VehicleResponse(10L, 1L, "SEDAN", "Toyota", "Camry", "REG-888", "Blue");

        when(vehicleService.createVehicle(any(VehicleCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.registrationNumber").value("REG-888"));
    }

    @Test
    @DisplayName("GET /api/vehicles/driver/{driverId} - Get Vehicle By Driver")
    void getVehicleByDriver() throws Exception {
        VehicleResponse response = new VehicleResponse(10L, 1L, "SEDAN", "Toyota", "Camry", "REG-888", "Blue");
        when(vehicleService.getVehicleResponseByDriverId(1L)).thenReturn(response);

        mockMvc.perform(get("/api/vehicles/driver/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.driverId").value(1L));
    }
}
