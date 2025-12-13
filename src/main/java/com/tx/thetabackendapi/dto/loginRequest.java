package com.tx.thetabackendapi.dto;

import jakarta.validation.constraints.NotBlank;


public record loginRequest(
        @NotBlank(message = "No username")
        String username,
        @NotBlank(message = "No password")
        String password
) {}