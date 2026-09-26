package com.ridelink.ridemanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI rideManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ride Management Service API")
                        .description("Microservice for managing ride lifecycles, passenger ride requests, driver assignments, and status transitions in RideLink.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("RideLink Group Project Team")
                                .email("dev@ridelink.com")));
    }
}
