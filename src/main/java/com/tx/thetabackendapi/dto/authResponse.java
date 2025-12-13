package com.tx.thetabackendapi.dto;


public record authResponse(String token, String username, Integer userId, String role, String message) {
    public static authResponse success(String token, String username, Integer userId, String role) {
        return new authResponse(token, username, userId, role, "200");
    }

    public static authResponse registered(String token, String username, Integer userId, String role) {
        return new authResponse(token, username, userId, role, "201");
    }

    public static authResponse error(String message) {
        return new authResponse(null, null, null, null, message);
    }
}