package com.petcare.admin.catalog.item.dto;

import com.petcare.common.common.embeddable.LocalizableString;

public record CreateItemRequest(
    LocalizableString name, LocalizableString description, Long categoryId, Long merchantId) {}
