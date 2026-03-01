package com.petcare.admin.catalog.item.dto;

import com.petcare.admin.catalog.item.domain.ItemStatus;
import com.petcare.common.asset.dto.AssetDto;
import com.petcare.common.catalog.domain.StockMode;
import com.petcare.common.common.embeddable.DateTimePeriod;
import com.petcare.common.common.embeddable.LocalizableString;
import java.math.BigDecimal;
import java.util.List;

public record VariationDetailsDto(
    Long id,
    LocalizableString name,
    LocalizableString description,
    String sku,
    ItemStatus status,
    BigDecimal price,
    BigDecimal salePrice,
    DateTimePeriod salePricePeriod,
    StockMode stockMode,
    Integer unitCapacity,
    Boolean hideWhenOutOfStock,
    Integer stockQty,
    Integer maxQtyPerCart,
    AssetDto thumbnail,
    List<AssetDto> assets) {}
