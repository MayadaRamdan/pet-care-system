package com.petcare.admin.zone.application;

import com.petcare.admin.merchant.domain.Merchant;
import com.petcare.admin.merchant.repository.MerchantRepository;
import com.petcare.admin.zone.domain.Zone;
import com.petcare.admin.zone.repository.ZoneRepository;
import com.petcare.common.exception.domain.ResourceNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class UpdateZoneMerchantsUseCase {

  private final ZoneRepository zoneRepository;
  private final MerchantRepository merchantRepository;

  public void execute(Long zoneId, List<Long> merchantIds) {
    Zone zone =
        zoneRepository
            .findById(zoneId)
            .orElseThrow(() -> new ResourceNotFoundException("zone", zoneId));

    List<Merchant> merchants = merchantRepository.findAllById(merchantIds);
    Set<Long> dbMerchantsIds = merchants.stream().map(Merchant::getId).collect(Collectors.toSet());
    for (Long id : merchantIds) {
      if (!dbMerchantsIds.contains(id)) {
        throw new ResourceNotFoundException("merchant" + id);
      }
    }

    zone.setMerchants(merchants);

    zoneRepository.save(zone);
  }
}
