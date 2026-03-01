package com.petcare.customer.security.dto;

import com.petcare.common.common.embeddable.Name;

public record UserInfo(Long id, String email, Name name, String avatarUrl) {}
