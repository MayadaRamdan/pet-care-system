package com.petcare.admin.catalog.category.application;

import com.petcare.admin.catalog.category.domain.CategoryListing;
import com.petcare.admin.catalog.category.dto.CategoryListingDto;
import com.petcare.admin.catalog.category.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ListCategoriesByParentIdUseCase {

  private final CategoryRepository categoryRepository;

  public List<CategoryListingDto> execute(Long parentId) {
    List<CategoryListing> children = categoryRepository.findSortedByParentId(parentId);
    return children.stream().map(this::map).collect(Collectors.toList());
  }

  private CategoryListingDto map(CategoryListing category) {
    return new CategoryListingDto(category.getId(), category.getName(), category.getStatus());
  }
}
