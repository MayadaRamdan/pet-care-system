package com.petcare.admin.catalog.item.application;

import com.petcare.admin.catalog.item.domain.ItemStatus;
import com.petcare.admin.catalog.item.repository.ItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class DeactivateItemUseCase {

  private final ItemRepository itemRepository;

  public void execute(Long itemId) {
    itemRepository.updateStatusById(itemId, ItemStatus.INACTIVE);
  }
}
