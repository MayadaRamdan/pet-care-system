package com.petcare.admin.security.dto;

import com.petcare.admin.security.domain.StaffUserPermission;
import java.util.Set;

public record UserInfo(
    Long id,
    String username,
    String fullName,
    String email,
    String roleName,
    Set<StaffUserPermission> permissions) {}
