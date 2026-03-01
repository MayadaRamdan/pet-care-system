package com.petcare.admin.catalog.item.application;

import com.petcare.admin.catalog.item.domain.Item;
import com.petcare.admin.catalog.item.domain.ItemStatus;
import com.petcare.admin.catalog.item.repository.ItemRepository;
import com.petcare.common.exception.domain.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ActivateItemUseCase {

  private final ItemRepository itemRepository;

  public void execute(Long itemId) {
    log.info("Activating item with id: {}", itemId);

    Item item =
        itemRepository.findById(itemId).orElseThrow(() -> ResourceNotFoundException.from("Item"));

    if (item.getThumbnail() == null && (item.getAssets() == null || item.getAssets().isEmpty())) {
      throw new IllegalArgumentException("Item must have a thumbnail or assets");
    }

    itemRepository.updateStatusById(itemId, ItemStatus.ACTIVE);
  }
}
