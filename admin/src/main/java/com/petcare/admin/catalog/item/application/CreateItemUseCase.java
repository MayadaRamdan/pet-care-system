package com.petcare.admin.catalog.item.application;

import com.petcare.admin.catalog.category.domain.Category;
import com.petcare.admin.catalog.category.repository.CategoryRepository;
import com.petcare.admin.catalog.item.domain.Item;
import com.petcare.admin.catalog.item.domain.ItemStatus;
import com.petcare.admin.catalog.item.dto.ItemDetailsDto;
import com.petcare.admin.catalog.item.repository.ItemRepository;
import com.petcare.admin.merchant.domain.Merchant;
import com.petcare.admin.merchant.repository.MerchantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class CreateItemUseCase {

  private final ItemRepository itemRepository;
  private final CategoryRepository categoryRepository;
  private final MerchantRepository merchantRepository;
  private final SyncItemVariationsUseCase syncItemVariationsUseCase;

  public void execute(ItemDetailsDto request) {

    Item item = new Item();

    item.setName(request.name());
    item.setStatus(ItemStatus.PENDING);
    item.setDeleted(false);
    item.setActive(false);

    if (request.category() != null) {
      Category category = categoryRepository.findById(request.category().id()).orElseThrow();
      item.setCategory(category);
    }

    if (request.merchant() != null) {
      Merchant merchant = merchantRepository.findById(request.merchant().id()).orElseThrow();
      item.setMerchant(merchant);
    }

    Item saved = itemRepository.save(item);
    syncItemVariationsUseCase.execute(saved, request.variations());
  }
}
