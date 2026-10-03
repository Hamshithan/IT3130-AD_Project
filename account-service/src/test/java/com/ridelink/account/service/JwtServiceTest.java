package com.ridelink.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    // Base64 encoded key for testing (at least 256 bits)
    private final String secret = "dGhpc2lzYXNlY3JldGtleWZvcmp3dGF1dGhlbnRpY2F0aW9uaW5hY2NvdW50c2VydmljZXJpZGVsaW5r";
    private final long expiration = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(secret, expiration);
    }

    @Test
    void generateToken_AndValidate_ShouldSucceed() {
        String token = jwtService.generateToken(100L, "john@example.com", "PASSENGER");

        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token));
        assertEquals(100L, jwtService.getUserId(token));
        assertEquals("john@example.com", jwtService.getEmail(token));
        assertEquals("PASSENGER", jwtService.getRole(token));
    }

    @Test
    void isTokenValid_InvalidToken_ShouldReturnFalse() {
        assertFalse(jwtService.isTokenValid("invalid.token.string"));
    }
}
