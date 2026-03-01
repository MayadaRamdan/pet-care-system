package com.petcare.admin.zonemerchant.dto;

import java.util.List;

public record UpdateZoneMerchantRequest(Long zoneId, List<Long> merchantIds) {}
