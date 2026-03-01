package com.petcare.customer.security.dto;

import com.petcare.common.common.embeddable.Name;

public record CustomerRegisterRequest(String email, String password, Name name) {}
