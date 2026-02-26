package com.petcare.admin.catalog.item.application;

import com.petcare.admin.catalog.item.domain.Item;
import com.petcare.admin.catalog.item.domain.Variation;
import com.petcare.admin.catalog.item.domain.VariationAsset;
import com.petcare.admin.catalog.item.dto.StockDetailsDto;
import com.petcare.admin.catalog.item.dto.VariationDetailsDto;
import com.petcare.admin.catalog.item.repository.ItemRepository;
import com.petcare.admin.catalog.item.repository.StockRepository;
import com.petcare.admin.catalog.item.repository.VariationAssetRepository;
import com.petcare.admin.catalog.item.repository.VariationRepository;
import com.petcare.common.asset.domain.Asset;
import com.petcare.common.asset.dto.AssetDto;
import com.petcare.common.asset.repository.AssetRepository;
import com.petcare.common.catalog.domain.Stock;
import com.petcare.common.catalog.domain.StockDetails;
import com.petcare.common.catalog.domain.StockMode;
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
  private final VariationAssetRepository variationAssetRepository;
  private final StockRepository stockRepository;

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

      // applyThumbnail(variation, dto.thumbnail());
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

    // Cascade persists new/updated variations
    itemRepository.save(item);

    // Clean in-memory collection after deletes
    item.getVariations().removeIf(v -> v.getId() != null && !keptIds.contains(v.getId()));

    // Sync stock for variations based on stock mode
    syncVariationsStock(item, incoming);
  }

  private void syncVariationsStock(Item item, List<VariationDetailsDto> incoming) {
    StockMode stockMode = (item.getStockMode() == null) ? StockMode.DEDICATED : item.getStockMode();

    switch (stockMode) {
      case SHARED:
        syncSharedStock(item, incoming);
        break;
      case DEDICATED:
        syncDedicatedStock(item, incoming);
        break;
    }
  }

  private void syncSharedStock(Item item, List<VariationDetailsDto> incoming) {
    // For shared mode, all variations share one stock object
    // Use the stockDetails from the variation with minimum unitCapacity
    StockDetailsDto minUnitCapacityStockDetails =
        incoming.stream()
            .filter(dto -> dto != null && dto.stockDetails() != null)
            .map(VariationDetailsDto::stockDetails)
            .min(
                (s1, s2) -> {
                  Integer cap1 = s1.unitCapacity() != null ? s1.unitCapacity() : 1;
                  Integer cap2 = s2.unitCapacity() != null ? s2.unitCapacity() : 1;
                  return cap1.compareTo(cap2);
                })
            .orElse(null);

    if (minUnitCapacityStockDetails == null) {
      return;
    }

    // Find or create the shared stock
    Stock sharedStock =
        item.getVariations().stream()
            .filter(v -> v.getStockDetails() != null && v.getStockDetails().getStock() != null)
            .map(v -> v.getStockDetails().getStock())
            .findFirst()
            .orElseGet(
                () -> {
                  Stock newStock = new Stock();
                  newStock.setQuantity(0);
                  return stockRepository.save(newStock);
                });

    // Update shared stock quantity based on min unitCapacity
    sharedStock.setQuantity(
        minUnitCapacityStockDetails.stockQty() != null
            ? minUnitCapacityStockDetails.stockQty()
                * (minUnitCapacityStockDetails.unitCapacity() != null
                    ? minUnitCapacityStockDetails.unitCapacity()
                    : 1)
            : 0);
    stockRepository.save(sharedStock);

    // Apply the same stock to all variations with their own stockDetails
    Map<Long, StockDetailsDto> stockByVariationId = new HashMap<>();
    for (VariationDetailsDto dto : incoming) {
      if (dto != null && dto.id() != null && dto.stockDetails() != null) {
        stockByVariationId.put(dto.id(), dto.stockDetails());
      }
    }

    for (Variation variation : item.getVariations()) {
      if (variation.getId() != null) {
        StockDetailsDto stockDto = stockByVariationId.get(variation.getId());
        if (stockDto == null) {
          continue;
        }

        StockDetails stockDetails = new StockDetails();
        stockDetails.setStock(sharedStock);
        stockDetails.setUnitCapacity(stockDto.unitCapacity());
        stockDetails.setHideWhenOutOfStock(stockDto.hideWhenOutOfStock());
        variation.setStockDetails(stockDetails);
      }
    }
  }

  private void syncDedicatedStock(Item item, List<VariationDetailsDto> incoming) {
    // For dedicated mode, each variation has its own stock object
    Map<Long, StockDetailsDto> stockByVariationId = new HashMap<>();
    for (VariationDetailsDto dto : incoming) {
      if (dto != null && dto.id() != null && dto.stockDetails() != null) {
        stockByVariationId.put(dto.id(), dto.stockDetails());
      }
    }

    for (Variation variation : item.getVariations()) {
      if (variation.getId() == null) {
        continue;
      }

      StockDetailsDto stockDto = stockByVariationId.get(variation.getId());
      if (stockDto == null) {
        continue;
      }

      // Find or create stock for this variation
      Stock stock;
      if (variation.getStockDetails() != null && variation.getStockDetails().getStock() != null) {
        stock = variation.getStockDetails().getStock();
      } else {
        stock = new Stock();
        stock.setQuantity(0);
        stock = stockRepository.save(stock);
      }

      // Update stock quantity
      stock.setQuantity(
          stockDto.stockQty() != null
              ? stockDto.stockQty()
                  * (stockDto.unitCapacity() != null ? stockDto.unitCapacity() : 1)
              : 0);
      stockRepository.save(stock);

      // Update variation's stock details
      StockDetails stockDetails = new StockDetails();
      stockDetails.setStock(stock);
      stockDetails.setUnitCapacity(stockDto.unitCapacity());
      stockDetails.setHideWhenOutOfStock(stockDto.hideWhenOutOfStock());
      variation.setStockDetails(stockDetails);
    }
  }

  private void copyBasicInfo(Variation target, VariationDetailsDto dto) {
    target.setName(dto.name());
    target.setDescription(dto.description());
    target.setSku(dto.sku());
    target.setStatus(dto.status());
    target.setPrice(dto.price());
    target.setSalePrice(dto.salePrice());
    target.setSalePricePeriod(dto.salePricePeriod());
    target.setMaxQtyPerCart(dto.maxQtyPerCart());
    // target.setStockQty(dto.stockQty());
    // target.setHideWhenOutOfStock(dto.hideWhenOutOfStock());
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
