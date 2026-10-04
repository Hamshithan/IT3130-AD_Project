package com.ridelink.ridemanagement.client;

import com.ridelink.ridemanagement.client.dto.DriverAvailabilityResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpDriverVehicleClient implements DriverVehicleClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public HttpDriverVehicleClient(
            RestTemplate restTemplate,
            @Value("${driver-vehicle.base-url}") String baseUrl) {

        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Override
    public boolean isDriverAvailable(Long driverId) {

        if (driverId == null) {
            return false;
        }

        try {
            String url = baseUrl + "/api/drivers/" + driverId;

            ResponseEntity<DriverAvailabilityResponse> response =
                    restTemplate.getForEntity(
                            url,
                            DriverAvailabilityResponse.class
                    );

            DriverAvailabilityResponse driver = response.getBody();

            return driver != null && driver.isAvailable();

        } catch (RestClientException e) {
            return false;
        }
    }
}