package com.tx.thetabackendapi.dto;

import jakarta.validation.constraints.NotBlank;


public record loginRequest(
    @NotBlank(message = "No email")
    String email,
    
    @NotBlank(message = "Password is required")
    String password
) {}