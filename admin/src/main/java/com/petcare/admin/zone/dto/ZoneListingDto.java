package com.petcare.admin.zone.dto;

import com.petcare.admin.zone.domain.ZoneStatus;

public record ZoneListingDto(Long id, String code, String name, ZoneStatus status) {}
