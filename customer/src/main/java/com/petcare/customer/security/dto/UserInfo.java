package com.petcare.customer.security.dto;

import java.util.Set;

public record UserInfo(
    Long id, String username, String fullName, String email, Set<String> role, String avatarUrl) {}
