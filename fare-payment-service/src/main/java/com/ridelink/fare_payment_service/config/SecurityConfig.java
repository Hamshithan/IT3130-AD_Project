package com.ridelink.fare_payment_service.config;

import com.ridelink.fare_payment_service.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)

            .formLogin(AbstractHttpConfigurer::disable)

            .httpBasic(AbstractHttpConfigurer::disable)

            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )

            .authorizeHttpRequests(auth -> auth

    // Public
    .requestMatchers("/api/auth/login").permitAll()

    .requestMatchers(
            "/swagger-ui/**",
            "/v3/api-docs/**"
    ).permitAll()

    // Fare
    .requestMatchers(HttpMethod.POST, "/api/fares/calculate")
    .hasAnyRole("PASSENGER", "DRIVER")

    .requestMatchers(HttpMethod.GET, "/api/fares/**")
    .hasAnyRole("PASSENGER", "DRIVER", "ADMIN")

    // Payment
    .requestMatchers(HttpMethod.POST, "/api/payments")
    .hasRole("PASSENGER")

    .requestMatchers(HttpMethod.GET, "/api/payments/**")
    .hasAnyRole("PASSENGER", "ADMIN")

    // Receipt
    .requestMatchers(HttpMethod.POST, "/api/receipts")
    .hasAnyRole("PASSENGER", "ADMIN")

    .requestMatchers(HttpMethod.GET, "/api/receipts/**")
    .hasAnyRole("PASSENGER", "ADMIN")

    .anyRequest().authenticated()
)

            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}