package com.petcare.admin.catalog.item.application;

import com.petcare.admin.catalog.category.repository.CategoryRepository;
import com.petcare.admin.catalog.item.domain.CategoryProduct;
import com.petcare.admin.catalog.item.domain.Item;
import com.petcare.admin.catalog.item.domain.Variation;
import com.petcare.admin.catalog.item.dto.ItemDetailsDto;
import com.petcare.admin.catalog.item.dto.VariationDetailsDto;
import com.petcare.admin.catalog.item.repository.ItemRepository;
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

    return new ItemDetailsDto(
        item.getId(),
        item.getName(),
        item.getDescription(),
        item.getStatus(),
        IdNameMapper.toIdName(item.getCategory().getId(), item.getCategory().getName()),
        IdNameMapper.toIdName(item.getMerchant().getId(), item.getMerchant().getName()),
        variations,
        assetMapper.toDto(item.getThumbnail()),
        getSecondaryCategories(item));
  }

  private List<IdName> getSecondaryCategories(Item item) {
    List<CategoryProduct> categories = item.getCategories();
    Long mainCategoryId = item.getCategory().getId();

    if (categories == null || categories.size() < 2) {
      return List.of();
    }

    Set<Long> secondaryCategoriesIds =
        categories.stream()
            .map(CategoryProduct::getCategoryId)
            .filter(id -> !id.equals(mainCategoryId))
            .collect(Collectors.toSet());
    if (secondaryCategoriesIds.isEmpty()) {
      return List.of();
    }
    return categoryRepository.listCategoriesByIdIn(secondaryCategoriesIds);
  }

  private VariationDetailsDto toVariationDetailsDto(Variation v) {
    return new VariationDetailsDto(
        v.getId(),
        v.getName(),
        v.getDescription(),
        v.getSku(),
        v.getStatus(),
        v.getPrice(),
        v.getSalePrice(),
        v.getSalePricePeriod(),
        v.getStockQty(),
        v.getMaxQtyPerOrder(),
        assetMapper.toDto(v.getThumbnail()));
  }
}
