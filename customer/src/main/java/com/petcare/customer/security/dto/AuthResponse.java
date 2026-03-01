package com.petcare.customer.security.dto;

public record AuthResponse(
    String accessToken,
    String tokenType,
    UserInfo user) {}
