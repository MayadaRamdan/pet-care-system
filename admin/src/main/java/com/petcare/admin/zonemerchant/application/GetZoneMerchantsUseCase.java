package com.petcare.admin.zonemerchant.application;

import com.petcare.admin.zonemerchant.dto.ZoneMerchantsResponse;
import com.petcare.admin.zonemerchant.repository.ZoneMerchantRepository;
import com.petcare.common.common.dto.IdName;
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
    List<IdName> merchants = zoneMerchantRepository.findMerchantsForZone(zoneId);
    return new ZoneMerchantsResponse(merchants);
  }
}
