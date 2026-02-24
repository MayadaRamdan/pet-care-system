package com.petcare.customer.catalog.domain;

import com.petcare.common.common.embeddable.LocalizableString;

public record CategoryFlatRow(
    Long id, LocalizableString name, LocalizableString path, String thumbnailUrl, Long parentId, int displayOrder) {}
