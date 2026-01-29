package com.petcare.admin.catalog.item.dto;

import com.petcare.admin.catalog.item.domain.ItemStatus;
import com.petcare.common.asset.dto.AssetDto;
import com.petcare.common.common.embeddable.DateTimePeriod;
import com.petcare.common.common.embeddable.LocalizableString;
import java.math.BigDecimal;

public record VariationDetailsDto(
    Long id,
    LocalizableString name,
    LocalizableString description,
    String sku,
    ItemStatus status,
    BigDecimal price,
    BigDecimal salePrice,
    DateTimePeriod salePricePeriod,
    Integer stockQty,
    Integer maxQtyPerOrder,
    AssetDto thumbnail) {}
