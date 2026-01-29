package com.petcare.admin.catalog.item.application;

import com.petcare.admin.catalog.item.domain.Item;
import com.petcare.admin.catalog.item.domain.Variation;
import com.petcare.admin.catalog.item.dto.VariationDetailsDto;
import com.petcare.admin.catalog.item.repository.ItemRepository;
import com.petcare.admin.catalog.item.repository.VariationRepository;
import com.petcare.common.asset.domain.Asset;
import com.petcare.common.asset.dto.AssetDto;
import com.petcare.common.asset.repository.AssetRepository;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class SyncItemVariationsUseCase {

  private final ItemRepository itemRepository;
  private final VariationRepository variationRepository;
  private final AssetRepository assetRepository;

  public void execute(Long itemId, List<VariationDetailsDto> variations) {
    execute(itemRepository.findById(itemId).orElseThrow(), variations);
  }

  public void execute(Item item, List<VariationDetailsDto> incoming) {
    Long itemId = item.getId();

    Map<Long, Variation> existingById = new HashMap<>();
    for (Variation v : item.getVariations()) {
      if (v.getId() != null) {
        existingById.put(v.getId(), v);
      }
    }

    Set<Long> keptIds = new HashSet<>();

    for (VariationDetailsDto dto : incoming) {
      if (dto == null) {
        continue;
      }

      Variation variation;
      if (dto.id() == null) {
        variation = new Variation();
        variation.setItem(item);
        item.getVariations().add(variation);
      } else {
        variation = existingById.get(dto.id());
        if (variation == null) {
          throw new IllegalArgumentException(
              "Variation id " + dto.id() + " does not belong to item id " + itemId);
        }
        keptIds.add(dto.id());
      }
      copyBasicInfo(variation, dto);

      applyThumbnail(variation, dto.thumbnail());
    }

    // Soft-delete removed variations (Variation has @SQLDelete)
    item.getVariations().stream()
        .filter(v -> v.getId() != null)
        .filter(v -> !keptIds.contains(v.getId()))
        .toList()
        .forEach(
            v -> {
              log.info("Soft deleting variation {} from item {}", v.getId(), itemId);
              variationRepository.delete(v);
            });

    // Cascade persists new/updated variations
    itemRepository.save(item);

    // Clean in-memory collection after deletes
    item.getVariations().removeIf(v -> v.getId() != null && !keptIds.contains(v.getId()));
  }

  private void copyBasicInfo(Variation target, VariationDetailsDto dto) {
    target.setName(dto.name());
    target.setDescription(dto.description());
    target.setSku(dto.sku());
    target.setStatus(dto.status());
    target.setPrice(dto.price());
    target.setSalePrice(dto.salePrice());
    target.setSalePricePeriod(dto.salePricePeriod());
    target.setStockQty(dto.stockQty());
    target.setMaxQtyPerOrder(dto.maxQtyPerOrder());
  }

  private void applyThumbnail(Variation target, AssetDto thumbnailDto) {
    if (thumbnailDto == null) {
      target.setThumbnail(null);
      target.setThumbnailUrl(null);
      return;
    }

    Long assetId = thumbnailDto.id();
    if (assetId == null) {
      throw new IllegalArgumentException("thumbnail.id must not be null");
    }

    Asset asset = assetRepository.findById(assetId).orElseThrow();
    target.setThumbnail(asset);
    target.setThumbnailUrl(asset.getUrl());
  }
}
