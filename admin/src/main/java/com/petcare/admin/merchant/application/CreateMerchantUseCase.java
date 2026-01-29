package com.petcare.admin.merchant.application;

import com.petcare.admin.merchant.domain.Merchant;
import com.petcare.admin.merchant.domain.MerchantStatus;
import com.petcare.admin.merchant.dto.CreateMerchantRequest;
import com.petcare.admin.merchant.repository.MerchantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class CreateMerchantUseCase {

  private final MerchantRepository merchantRepository;

  public void execute(CreateMerchantRequest request) {
    Merchant merchant = new Merchant();
    merchant.setDeleted(false);
    merchant.setActive(true);
    merchant.setName(request.name());
    merchant.setStatus(MerchantStatus.ACTIVE);

    merchantRepository.save(merchant);
  }
}
