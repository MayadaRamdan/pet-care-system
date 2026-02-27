package com.petcare.customer.security.dto;


public record UserInfo(
    Long id, String username, String fullName, String email, String avatarUrl) {}
