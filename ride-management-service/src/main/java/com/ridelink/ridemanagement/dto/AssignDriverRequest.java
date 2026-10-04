package com.ridelink.ridemanagement.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignDriverRequest {

    @NotNull(message = "Driver ID is required")
    private Long driverId;
}
