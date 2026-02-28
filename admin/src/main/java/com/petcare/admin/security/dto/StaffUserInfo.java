package com.petcare.admin.security.dto;

import com.petcare.admin.security.domain.StaffUserPermission;
import com.petcare.common.common.embeddable.Name;
import java.util.Set;

public record StaffUserInfo(
    Long id,
    String username,
    Name name,
    String email,
    String roleName,
    Set<StaffUserPermission> permissions) {}
