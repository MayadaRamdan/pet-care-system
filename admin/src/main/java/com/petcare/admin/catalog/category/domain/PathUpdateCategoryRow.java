package com.petcare.admin.catalog.category.domain;

public record PathUpdateCategoryRow(
    Long id, String englishName, String arabicName, Long parentId) {}
