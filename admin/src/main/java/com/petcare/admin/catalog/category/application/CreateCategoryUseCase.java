package com.petcare.admin.catalog.category.application;

import com.petcare.admin.catalog.category.domain.Category;
import com.petcare.admin.catalog.category.domain.CategoryStatus;
import com.petcare.admin.catalog.category.dto.CreateCategoryRequest;
import com.petcare.admin.catalog.category.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class CreateCategoryUseCase {

  private final CategoryRepository categoryRepository;

  public void execute(CreateCategoryRequest request) {

    Category category = new Category();

    category.setLevel(0);

    if (request.parentId() != null) {
      Category parent = categoryRepository.findById(request.parentId()).orElseThrow();
      category.setParent(parent);
      category.setLevel(parent.getLevel() + 1);
    }

    category.setName(request.name());
    category.setStatus(CategoryStatus.INACTIVE);
    category.setDeleted(false);
    category.setActive(false);

    categoryRepository.save(category);
  }
}
