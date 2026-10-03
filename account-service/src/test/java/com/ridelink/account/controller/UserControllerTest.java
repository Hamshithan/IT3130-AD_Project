package com.ridelink.account.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridelink.account.dto.LoginResponse;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;
    private String jwtToken;
    private Long userId;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        String uniqueEmail = "user" + System.currentTimeMillis() + "@example.com";

        String registerJson = String.format("""
                {
                    "name": "Jane User",
                    "email": "%s",
                    "password": "password123",
                    "phone": "0779876543",
                    "role": "DRIVER"
                }
                """, uniqueEmail);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isCreated());

        String loginJson = String.format("""
                {
                    "email": "%s",
                    "password": "password123"
                }
                """, uniqueEmail);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        LoginResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                LoginResponse.class
        );

        this.jwtToken = response.getToken();
        this.userId = response.getId();
    }

    @Test
    void getUser_Authenticated_ReturnsUser() throws Exception {
        mockMvc.perform(get("/api/v1/users/" + userId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Jane User"));
    }

    @Test
    void getUser_Unauthenticated_Returns403Or401() throws Exception {
        mockMvc.perform(get("/api/v1/users/" + userId))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void updateUser_Authenticated_Success() throws Exception {
        String updateJson = """
                {
                    "name": "Jane Updated",
                    "phone": "0770000000"
                }
                """;

        mockMvc.perform(put("/api/v1/users/" + userId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Updated"))
                .andExpect(jsonPath("$.phone").value("0770000000"));
    }

    @Test
    void deleteUser_Authenticated_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/users/" + userId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNoContent());

        // Verify fetching deleted user returns 404
        mockMvc.perform(get("/api/v1/users/" + userId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }
}
