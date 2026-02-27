package com.petcare.admin.zonemerchant.application;

import com.petcare.admin.merchant.domain.Merchant;
import com.petcare.admin.merchant.repository.MerchantRepository;
import com.petcare.admin.zone.domain.Zone;
import com.petcare.admin.zone.repository.ZoneRepository;
import com.petcare.admin.zonemerchant.domain.ZoneMerchant;
import com.petcare.admin.zonemerchant.dto.UpdateZoneStoresRequest;
import com.petcare.admin.zonemerchant.repository.ZoneMerchantRepository;
import com.petcare.common.exception.domain.ResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class UpdateZoneStoresUseCase {

  private final ZoneRepository zoneRepository;
  private final MerchantRepository merchantRepository;
  private final ZoneMerchantRepository zoneMerchantRepository;

  public void execute(UpdateZoneStoresRequest request) {
    log.info("Updating stores for zoneId={}, storeIds={}", request.zoneId(), request.storeIds());

    Zone zone =
        zoneRepository
            .findById(request.zoneId())
            .orElseThrow(() -> new ResourceNotFoundException("Zone" + request.zoneId()));

    List<Merchant> merchants = merchantRepository.findAllById(request.storeIds());

    validateAllMerchantsFound(request.storeIds(), merchants);

    List<ZoneMerchant> zoneMerchants =
        merchants.stream().map(merchant -> ZoneMerchant.of(zone, merchant)).toList();

    zoneMerchantRepository.deleteByZoneId(request.zoneId());

    zoneMerchantRepository.saveAll(zoneMerchants);

    log.info(
        "Successfully updated {} stores for zoneId={}", zoneMerchants.size(), request.zoneId());
  }

  private void validateAllMerchantsFound(List<Long> requestedIds, List<Merchant> foundMerchants) {
    if (foundMerchants.size() == requestedIds.size()) return;

    List<Long> foundIds = foundMerchants.stream().map(Merchant::getId).toList();

    List<Long> missingIds = requestedIds.stream().filter(id -> !foundIds.contains(id)).toList();

    String missingIdsString =
        missingIds.stream().map(String::valueOf).collect(Collectors.joining(", "));

    throw new ResourceNotFoundException("Merchants", missingIdsString);
  }
}
