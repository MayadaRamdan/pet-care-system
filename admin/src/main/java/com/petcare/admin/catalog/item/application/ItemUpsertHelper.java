package com.petcare.admin.catalog.item.application;

import com.petcare.admin.catalog.category.repository.CategoryRepository;
import com.petcare.admin.catalog.item.domain.CategoryItem;
import com.petcare.admin.catalog.item.domain.Item;
import com.petcare.admin.catalog.item.domain.ItemAsset;
import com.petcare.admin.catalog.item.domain.ItemStatus;
import com.petcare.admin.catalog.item.dto.ItemDetailsDto;
import com.petcare.admin.catalog.item.repository.CategoryItemRepository;
import com.petcare.admin.catalog.item.repository.ItemAssetRepository;
import com.petcare.admin.catalog.item.repository.ItemRepository;
import com.petcare.admin.merchant.repository.MerchantRepository;
import com.petcare.common.asset.domain.Asset;
import com.petcare.common.asset.dto.AssetDto;
import com.petcare.common.asset.repository.AssetRepository;
import com.petcare.common.catalog.domain.StockMode;
import com.petcare.common.common.dto.IdName;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ItemUpsertHelper {

  private final ItemRepository itemRepository;
  private final CategoryRepository categoryRepository;
  private final MerchantRepository merchantRepository;
  private final SyncItemVariationsUseCase syncItemVariationsUseCase;
  private final CategoryItemRepository categoryItemRepository;
  private final AssetRepository assetRepository;
  private final ItemAssetRepository itemAssetRepository;

  public void upsert(ItemDetailsDto request, Item item) {
    if (item == null) {
      item = new Item();
      item.setDeleted(false);
      item.setActive(false);
      item.setStatus(ItemStatus.PENDING);
    }

    item.setName(request.name());
    item.setDescription(request.description());

    item.setCategory(categoryRepository.findById(request.category().id()).orElseThrow());
    item.setMerchant(merchantRepository.findById(request.merchant().id()).orElseThrow());

    item.setStockMode(request.stockMode());
    if (request.stockMode() == null) {
      item.setStockMode(StockMode.DEDICATED);
    }

    item.setMaxQtyPerCart(request.maxQtyPerCart());
    item.setHideWhenOutOfStock(request.hideWhenOutOfStock());

    Item saved = itemRepository.save(item);

    syncItemCategories(item, request.categories(), request.category());
    syncItemAssets(item, request.assets(), request.thumbnail());

    syncItemVariationsUseCase.execute(saved, request.variations());
  }

  private void syncItemCategories(Item item, List<IdName> categories, IdName category) {
    Set<Long> finalCategoriesIds = new HashSet<>();
    finalCategoriesIds.add(category.id());

    if (categories != null && !categories.isEmpty()) {
      categories.forEach(c -> finalCategoriesIds.add(c.id()));
    }

    if (item.getCategories() != null && !item.getCategories().isEmpty()) {
      Set<Long> categoriesToBeRemoved = new HashSet<>();
      for (CategoryItem ci : item.getCategories()) {
        if (finalCategoriesIds.contains(ci.getCategoryId())) {
          finalCategoriesIds.remove(ci.getCategoryId());
        } else {
          categoriesToBeRemoved.add(ci.getCategoryId());
        }
      }
      if (!categoriesToBeRemoved.isEmpty()) {
        categoryItemRepository.deleteByItemIdAndCategoriesIdsIn(
            item.getId(), categoriesToBeRemoved);
      }
    }

    if (finalCategoriesIds.isEmpty()) {
      return;
    }

    Set<CategoryItem> itemCategories = new HashSet<>();
    categoryRepository
        .getByIds(finalCategoriesIds)
        .forEach(c -> itemCategories.add(CategoryItem.of(c, item)));

    categoryItemRepository.saveAll(itemCategories);
  }

  private void syncItemAssets(Item item, List<AssetDto> assets, AssetDto thumbnailDto) {
    Set<Long> finalAssetsIds = new HashSet<>();

    Long mainAssetId = null;
    if (thumbnailDto != null) {
      item.setThumbnail(assetRepository.findById(thumbnailDto.id()).orElseThrow());
      item.setThumbnailUrl(item.getThumbnail().getUrl());
      mainAssetId = thumbnailDto.id();
      finalAssetsIds.add(mainAssetId);
    }

    if (assets != null && !assets.isEmpty()) {
      assets.forEach(c -> finalAssetsIds.add(c.id()));
    }

    if (item.getAssets() != null && !item.getAssets().isEmpty()) {
      Set<Long> assetsToBeRemoved = new HashSet<>();
      for (ItemAsset ci : item.getAssets()) {
        if (finalAssetsIds.contains(ci.getAssetId())) {
          finalAssetsIds.remove(ci.getAssetId());
        } else {
          assetsToBeRemoved.add(ci.getAssetId());
        }
      }
      if (!assetsToBeRemoved.isEmpty()) {
        itemAssetRepository.deleteByItemIdAndAssetsIdsIn(item.getId(), assetsToBeRemoved);
      }
    }

    if (finalAssetsIds.isEmpty()) {
      return;
    }

    Set<ItemAsset> itemAssets = new HashSet<>();
    for (Asset a : assetRepository.getByIds(finalAssetsIds)) {
      itemAssets.add(ItemAsset.of(item, a, (mainAssetId != null && mainAssetId.equals(a.getId()))));
    }

    itemAssetRepository.saveAll(itemAssets);
  }
}
