package com.petcare.admin.catalog.item.application;

import com.petcare.admin.catalog.category.repository.CategoryRepository;
import com.petcare.admin.catalog.item.domain.CategoryItem;
import com.petcare.admin.catalog.item.domain.Item;
import com.petcare.admin.catalog.item.domain.Variation;
import com.petcare.admin.catalog.item.dto.ItemDetailsDto;
import com.petcare.admin.catalog.item.dto.VariationDetailsDto;
import com.petcare.admin.catalog.item.repository.ItemRepository;
import com.petcare.common.asset.dto.AssetDto;
import com.petcare.common.asset.mapper.AssetMapper;
import com.petcare.common.common.dto.IdName;
import com.petcare.common.common.mapper.IdNameMapper;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ItemDetailsUseCase {

  private final ItemRepository itemRepository;
  private final CategoryRepository categoryRepository;
  private final AssetMapper assetMapper;

  public ItemDetailsDto execute(Long itemId) {
    Item item =
        itemRepository
            .findById(itemId)
            .orElseThrow(() -> new IllegalArgumentException("Item not found: id=" + itemId));

    List<VariationDetailsDto> variations =
        item.getVariations() == null
            ? List.of()
            : item.getVariations().stream().map(this::toVariationDetailsDto).toList();

    List<AssetDto> assetDtos = null;
    if (item.getAssets() != null) {
      assetDtos =
          item.getAssets().stream()
              .map(ia -> assetMapper.toDto(ia.getAsset()))
              .collect(Collectors.toList());
    }

    return new ItemDetailsDto(
        item.getId(),
        item.getName(),
        item.getDescription(),
        item.getStatus(),
        IdNameMapper.toIdName(item.getMerchant().getId(), item.getMerchant().getName()),
        IdNameMapper.toIdName(item.getCategory().getId(), item.getCategory().getName()),
        getSecondaryCategories(item),
        item.getMaxQtyPerCart(),
        item.getStockMode(),
        item.getHideWhenOutOfStock(),
        item.getStockAvailable(),
        (item.getThumbnail() == null) ? null : assetMapper.toDto(item.getThumbnail()),
        assetDtos,
        variations);
  }

  private List<IdName> getSecondaryCategories(Item item) {
    List<CategoryItem> categories = item.getCategories();
    Long mainCategoryId = item.getCategory().getId();

    if (categories == null || categories.size() < 2) {
      return List.of();
    }

    Set<Long> secondaryCategoriesIds =
        categories.stream()
            .map(CategoryItem::getCategoryId)
            .filter(id -> !id.equals(mainCategoryId))
            .collect(Collectors.toSet());
    if (secondaryCategoriesIds.isEmpty()) {
      return List.of();
    }
    return categoryRepository.listCategoriesByIdIn(secondaryCategoriesIds);
  }

  private VariationDetailsDto toVariationDetailsDto(Variation v) {
    List<AssetDto> assetDtos = null;

    if (v.getAssets() != null) {
      assetDtos =
          v.getAssets().stream()
              .map(va -> assetMapper.toDto(va.getAsset()))
              .collect(Collectors.toList());
    }

    return new VariationDetailsDto(
        v.getId(),
        v.getName(),
        v.getDescription(),
        v.getSku(),
        v.getStatus(),
        v.getPrice(),
        v.getSalePrice(),
        v.getSalePricePeriod(),
        v.getStockMode(),
        v.getUnitCapacity(),
        v.getHideWhenOutOfStock(),
        v.getStockQty(),
        v.getMaxQtyPerCart(),
        assetMapper.toDto(v.getThumbnail()),
        assetDtos);
  }
}
