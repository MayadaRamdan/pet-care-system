package com.petcare.admin.catalog.item.application;

import com.petcare.admin.catalog.item.domain.Item;
import com.petcare.admin.catalog.item.dto.ItemDetailsDto;
import com.petcare.admin.catalog.item.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UpdateItemUseCase {

  private final ItemUpsertHelper itemUpsertHelper;
  private final ItemRepository itemRepository;

  public void execute(ItemDetailsDto request) {
    Item item = itemRepository.findById(request.id()).orElseThrow();
    itemUpsertHelper.upsert(request, item);
  }
}
