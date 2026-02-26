package com.petcare.admin.catalog.item.dto;

import com.petcare.admin.catalog.item.domain.ItemStatus;
import com.petcare.common.asset.dto.AssetDto;
import com.petcare.common.catalog.domain.StockMode;
import com.petcare.common.common.dto.IdName;
import com.petcare.common.common.embeddable.LocalizableString;
import java.util.List;

public record ItemDetailsDto(
    Long id,
    LocalizableString name,
    LocalizableString description,
    ItemStatus status,
    IdName merchant,
    IdName category,
    List<IdName> categories,
    Integer maxQtyPerCart,
    StockMode stockMode,
    Boolean hideWhenOutOfStock,
    Boolean stockAvailable,
    AssetDto thumbnail,
    List<AssetDto> assets,
    List<VariationDetailsDto> variations) {}
