package com.petcare.admin.zonemerchant.dto;

import java.util.List;

public record UpdateZoneStoresRequest(Long zoneId, List<Long> storeIds) {}
