package com.petcare.admin.staffuser.domain;

import com.petcare.common.common.embeddable.Name;

public record StaffUserCreateRequest(
    String email,
    String username,
    String password,
    Name name,
    String phoneCountryCode,
    String phoneNumber,
    Long roleId) {}
