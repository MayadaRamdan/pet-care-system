package com.petcare.admin.security.dto;

public record AuthResponse(String accessToken, String tokenType, StaffUserInfo user) {}
