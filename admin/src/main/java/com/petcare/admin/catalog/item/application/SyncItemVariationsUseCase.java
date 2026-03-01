package com.petcare.admin.catalog.item.application;

import com.petcare.admin.catalog.item.domain.Item;
import com.petcare.admin.catalog.item.domain.Variation;
import com.petcare.admin.catalog.item.domain.VariationAsset;
import com.petcare.admin.catalog.item.dto.VariationDetailsDto;
import com.petcare.admin.catalog.item.repository.ItemRepository;
import com.petcare.admin.catalog.item.repository.StockRepository;
import com.petcare.admin.catalog.item.repository.VariationAssetRepository;
import com.petcare.admin.catalog.item.repository.VariationRepository;
import com.petcare.common.asset.domain.Asset;
import com.petcare.common.asset.dto.AssetDto;
import com.petcare.common.asset.repository.AssetRepository;
import com.petcare.common.catalog.domain.Stock;
import com.petcare.common.catalog.domain.StockMode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
  private final VariationAssetRepository variationAssetRepository;
  private final StockRepository stockRepository;

  public void execute(Long itemId, List<VariationDetailsDto> variations) {
    execute(itemRepository.findById(itemId).orElseThrow(), variations);
  }

  public void execute(Item item, List<VariationDetailsDto> incoming) {
    Long itemId = item.getId();

    Stock sharedStock = null;
    if (item.getStockMode() == StockMode.SHARED) {
      sharedStock = buildCorrectStockForShared(item, incoming);
    }

    Map<Long, Variation> existingById = new HashMap<>();
    for (Variation v : item.getVariations()) {
      if (v.getId() != null) {
        existingById.put(v.getId(), v);
      }
    }

    for (VariationDetailsDto dto : incoming) {
      if (dto.id() != null && existingById.get(dto.id()) == null) {
        throw new IllegalArgumentException(
            "Variation id " + dto.id() + " does not belong to item id " + itemId);
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
        item.getVariations().add(variation);
      } else {
        variation = existingById.get(dto.id());
        keptIds.add(dto.id());
      }

      existingById.get(dto.id());

      if (item.getStockMode() == StockMode.SHARED) {
        variation.setStock(sharedStock);
      }

      copyBasicInfo(variation, dto, item);
      syncItemAssets(variation, dto.assets(), dto.thumbnail());
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

    // Clean in-memory collection after deletes
    item.getVariations().removeIf(v -> v.getId() != null && !keptIds.contains(v.getId()));
  }

  private Stock buildCorrectStockForShared(Item item, List<VariationDetailsDto> incoming) {

    VariationDetailsDto variationWithMinCapacity =
        incoming.stream()
            .filter(Objects::nonNull)
            .min(
                (s1, s2) -> {
                  Integer cap1 = s1.unitCapacity() != null ? s1.unitCapacity() : 1;
                  Integer cap2 = s2.unitCapacity() != null ? s2.unitCapacity() : 1;
                  return cap1.compareTo(cap2);
                })
            .orElse(null);

    int stockQty = 0;
    if (variationWithMinCapacity != null) {
      if (variationWithMinCapacity.stockQty() != null
          && variationWithMinCapacity.unitCapacity() != null) {
        stockQty = variationWithMinCapacity.stockQty() * variationWithMinCapacity.unitCapacity();
      }
    }

    Stock stock =
        item.getVariations().stream()
            .map(Variation::getStock)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);

    if (stock == null) {
      stock = stockRepository.save(Stock.of(stockQty));
    }

    stock.setQuantity(stockQty);

    return stock;
  }

  private void copyBasicInfo(Variation target, VariationDetailsDto dto, Item item) {
    target.setItem(item);
    target.setName(dto.name());
    target.setDescription(dto.description());
    target.setSku(dto.sku());
    target.setStatus(dto.status());
    target.setPrice(dto.price());
    target.setSalePrice(dto.salePrice());
    target.setSalePricePeriod(dto.salePricePeriod());
    target.setMaxQtyPerCart(dto.maxQtyPerCart());
    target.setHideWhenOutOfStock(dto.hideWhenOutOfStock());

    StockMode stockMode = item.getStockMode();
    target.setStockMode(stockMode);
    target.setUnitCapacity((stockMode == StockMode.SHARED) ? dto.unitCapacity() : 1);

    if (stockMode == StockMode.DEDICATED) {
      if (target.getStock() == null) {
        target.setStock(stockRepository.save(Stock.of(dto.stockQty())));
      } else {
        target.getStock().setQuantity(dto.stockQty());
      }
    }
  }

  private void syncItemAssets(Variation variation, List<AssetDto> assets, AssetDto thumbnailDto) {
    Set<Long> finalAssetsIds = new HashSet<>();

    Long mainAssetId = null;

    if (thumbnailDto == null) {
      variation.setThumbnail(null);
      variation.setThumbnailUrl(null);
    } else {
      variation.setThumbnail(assetRepository.findById(thumbnailDto.id()).orElseThrow());
      variation.setThumbnailUrl(variation.getThumbnail().getUrl());
      finalAssetsIds.add(thumbnailDto.id());
      mainAssetId = thumbnailDto.id();
    }

    if (assets != null && !assets.isEmpty()) {
      assets.forEach(c -> finalAssetsIds.add(c.id()));
    }

    if (variation.getAssets() != null && !variation.getAssets().isEmpty()) {
      Set<Long> assetsToBeRemoved = new HashSet<>();
      for (VariationAsset ci : variation.getAssets()) {
        if (finalAssetsIds.contains(ci.getAssetId())) {
          finalAssetsIds.remove(ci.getAssetId());
        } else {
          assetsToBeRemoved.add(ci.getAssetId());
        }
      }
      if (!assetsToBeRemoved.isEmpty()) {
        variationAssetRepository.deleteByVariationIdAndAssetsIdsIn(
            variation.getId(), assetsToBeRemoved);
      }
    }

    if (finalAssetsIds.isEmpty()) {
      return;
    }

    Set<VariationAsset> variationAssets = new HashSet<>();
    for (Asset a : assetRepository.getByIds(finalAssetsIds)) {
      variationAssets.add(
          VariationAsset.of(variation, a, (mainAssetId != null && mainAssetId.equals(a.getId()))));
    }

    variationAssetRepository.saveAll(variationAssets);
  }
}
