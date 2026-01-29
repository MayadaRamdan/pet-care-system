package com.petcare.admin.catalog.item.dto;

import com.petcare.admin.catalog.item.domain.ItemStatus;
import com.petcare.common.asset.dto.AssetDto;
import com.petcare.common.common.dto.IdName;
import com.petcare.common.common.embeddable.LocalizableString;
import java.util.List;

public record ItemDetailsDto(
    Long id,
    LocalizableString name,
    LocalizableString description,
    ItemStatus status,
    IdName category,
    IdName merchant,
    List<VariationDetailsDto> variations,
    AssetDto thumbnail,
    List<IdName> secondaryCategories) {}
