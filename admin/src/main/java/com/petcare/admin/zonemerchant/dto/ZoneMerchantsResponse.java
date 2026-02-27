package com.petcare.admin.zonemerchant.dto;

import com.petcare.admin.zonemerchant.domain.ZoneMerchantBasicInfo;

import java.util.List;

public record ZoneMerchantsResponse(List<ZoneMerchantBasicInfo> merchants) {}
