package com.petcare.customer.security.dto;


public record UserInfo(
    Long id, String email, String fullName, String avatarUrl) {}
