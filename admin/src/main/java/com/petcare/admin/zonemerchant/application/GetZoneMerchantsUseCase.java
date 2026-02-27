package com.petcare.admin.zonemerchant.application;

import com.petcare.admin.zonemerchant.domain.ZoneMerchantBasicInfo;
import com.petcare.admin.zonemerchant.dto.ZoneMerchantsResponse;
import com.petcare.admin.zonemerchant.repository.ZoneMerchantRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class GetZoneMerchantsUseCase {

  private final ZoneMerchantRepository zoneMerchantRepository;

  public ZoneMerchantsResponse execute(Long zoneId) {
    List<ZoneMerchantBasicInfo> merchants = zoneMerchantRepository.findMerchantsForZone(zoneId);
    return new ZoneMerchantsResponse(merchants);
  }
}
