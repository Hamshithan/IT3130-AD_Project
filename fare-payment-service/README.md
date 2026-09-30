# RideLink - Fare & Payment Service

## Overview

The Fare & Payment Service is one of the four core microservices of the RideLink ride-hailing system.

This service is responsible for:

- Fare estimation and calculation
- Simulated payment processing
- Payment status retrieval
- Receipt generation
- Receipt retrieval
- JWT authentication
- Role-based authorization

## Technologies Used

- Java 25
- Spring Boot
- Spring Web MVC
- Spring Data MongoDB
- Spring Security
- JWT
- Maven
- Swagger / OpenAPI
- JUnit 5
- Mockito
- MongoDB

## Service Information

**Service Name:** Fare & Payment Service

**Port:** `8084`

**Database:** MongoDB

**Database Name:** `ridelink_fare_payment`

## Fare Calculation

The service calculates the final fare using:

```text
Final Fare = Base Fare + (Distance × Per-KM Rate)