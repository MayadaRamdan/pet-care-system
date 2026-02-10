package com.petcare.customer.security.dto;

public record AuthResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    Long expiresIn, // seconds
    UserInfo user) {}
