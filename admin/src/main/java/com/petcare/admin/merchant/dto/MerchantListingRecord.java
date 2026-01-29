package com.petcare.admin.merchant.dto;

import com.petcare.admin.merchant.domain.MerchantStatus;

public record MerchantListingRecord(Long id, String name, MerchantStatus status) {}
