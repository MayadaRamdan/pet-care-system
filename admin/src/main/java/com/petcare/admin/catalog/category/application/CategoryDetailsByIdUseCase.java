package com.petcare.admin.catalog.category.application;

import com.petcare.admin.catalog.category.domain.Category;
import com.petcare.admin.catalog.category.dto.CategoryDetailsDto;
import com.petcare.admin.catalog.category.repository.CategoryRepository;
import com.petcare.common.asset.mapper.AssetMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CategoryDetailsByIdUseCase {

  private final CategoryRepository categoryRepository;
  private final AssetMapper assetMapper;

  public CategoryDetailsDto execute(Long id) {
    Category c = categoryRepository.findById(id).orElseThrow();
    return new CategoryDetailsDto(
        c.getId(), c.getName(), c.getStatus(), assetMapper.toDto(c.getThumbnail()));
  }
}
