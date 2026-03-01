package com.petcare.admin.catalog.item.application;

import com.petcare.admin.catalog.item.dto.ItemDetailsDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class CreateItemUseCase {

  private final ItemUpsertHelper itemUpsertHelper;

  public void execute(ItemDetailsDto request) {
    itemUpsertHelper.upsert(request, null);
  }
}
