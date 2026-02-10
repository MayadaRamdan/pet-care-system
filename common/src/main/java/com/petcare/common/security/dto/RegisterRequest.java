package com.petcare.common.security.dto;

public record RegisterRequest(String username, String password, String email, String fullName) {}
