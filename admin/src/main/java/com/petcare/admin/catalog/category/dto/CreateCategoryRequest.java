package com.petcare.admin.catalog.category.dto;

import com.petcare.common.common.embeddable.LocalizableString;

public record CreateCategoryRequest(LocalizableString name, Long parentId) {}
