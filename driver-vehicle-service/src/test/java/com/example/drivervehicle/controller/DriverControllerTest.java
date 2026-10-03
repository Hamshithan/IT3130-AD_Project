package com.example.drivervehicle.controller;

import com.example.drivervehicle.dto.DriverCreateRequest;
import com.example.drivervehicle.dto.DriverResponse;
import com.example.drivervehicle.exception.GlobalExceptionHandler;
import com.example.drivervehicle.service.DriverService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DriverControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DriverService driverService;

    @InjectMocks
    private DriverController driverController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(driverController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/drivers - Create Driver")
    void createDriver() throws Exception {
        DriverCreateRequest request = new DriverCreateRequest("Alice", "1234567890", "LIC-100", true, "Downtown", 10.0, 20.0);
        DriverResponse response = new DriverResponse(1L, "Alice", "1234567890", "LIC-100", true, "Downtown", 10.0, 20.0);

        when(driverService.createDriver(any(DriverCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.licenseNumber").value("LIC-100"));
    }

    @Test
    @DisplayName("GET /api/drivers - Get All Drivers")
    void getAllDrivers() throws Exception {
        DriverResponse response = new DriverResponse(1L, "Alice", "1234567890", "LIC-100", true, "Downtown", 10.0, 20.0);
        when(driverService.getAllDriverResponses()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/drivers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"));
    }

    @Test
    @DisplayName("GET /api/drivers/{id} - Get Driver By ID")
    void getDriverById() throws Exception {
        DriverResponse response = new DriverResponse(1L, "Alice", "1234567890", "LIC-100", true, "Downtown", 10.0, 20.0);
        when(driverService.getDriverResponseById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/drivers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("PATCH /api/drivers/{id}/availability - Update Availability")
    void updateAvailability() throws Exception {
        DriverResponse response = new DriverResponse(1L, "Alice", "1234567890", "LIC-100", false, "Downtown", 10.0, 20.0);
        when(driverService.updateAvailabilityResponse(1L, false)).thenReturn(response);

        mockMvc.perform(patch("/api/drivers/1/availability?available=false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    @DisplayName("GET /api/drivers/eligible - Get Eligible Drivers")
    void getEligibleDrivers() throws Exception {
        DriverResponse response = new DriverResponse(1L, "Alice", "1234567890", "LIC-100", true, "Downtown", 10.0, 20.0);
        when(driverService.getEligibleDriverResponses("Downtown")).thenReturn(List.of(response));

        mockMvc.perform(get("/api/drivers/eligible?serviceArea=Downtown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].serviceArea").value("Downtown"));
    }
}
