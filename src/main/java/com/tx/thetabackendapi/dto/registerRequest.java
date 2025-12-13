package com.tx.thetabackendapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record registerRequest(
        //using http response codes
        @NotBlank(message = "No username")
        @Size(min = 3, max = 20, message = "400")
        String username,
        @NotBlank(message = "No email")
        @Email(message = "400")
        String email,
        //Need to make password requirements more stringent
        @NotBlank(message = "No password")
        @Size(min = 6, message = "400")
        String password
) {}