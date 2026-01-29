package com.petcare.admin.catalog.category.dto;

import com.petcare.admin.catalog.category.domain.CategoryStatus;

public record CategoryListingDto(Long id, String name, CategoryStatus status) {}
