package com.petcare.admin.merchant.application;

import com.petcare.admin.merchant.domain.MerchantListing;
import com.petcare.admin.merchant.dto.MerchantListingRecord;
import com.petcare.admin.merchant.repository.MerchantRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ListMerchantsUseCase {

  private final MerchantRepository merchantRepository;

  public List<MerchantListingRecord> execute() {
    List<MerchantListing> merchants = merchantRepository.findAllNotDeleted();
    return merchants.stream().map(this::map).collect(Collectors.toList());
  }

  private MerchantListingRecord map(MerchantListing merchant) {
    return new MerchantListingRecord(merchant.getId(), merchant.getName(), merchant.getStatus());
  }
}
