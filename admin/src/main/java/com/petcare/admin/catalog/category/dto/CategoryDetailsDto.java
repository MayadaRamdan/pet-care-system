package com.petcare.admin.catalog.category.dto;

import com.petcare.admin.catalog.category.domain.CategoryStatus;
import com.petcare.common.asset.dto.AssetDto;
import com.petcare.common.common.embeddable.LocalizableString;

public record CategoryDetailsDto(
    Long id, LocalizableString name, CategoryStatus status, AssetDto thumbnail) {}
