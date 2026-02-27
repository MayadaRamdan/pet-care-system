package com.petcare.admin.staffuser.domain;

public record StaffUserCreateRequest(
    String email,
    String username,
    String password,
    String name,
    String phoneCountryCode,
    String phoneNumber,
    Long roleId) {}
