package com.petcare.admin.catalog.category.application;

import com.petcare.admin.catalog.category.domain.Category;
import com.petcare.admin.catalog.category.repository.CategoryRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class UpdateCategoriesPathsUseCase {

  private final CategoryRepository categoryRepository;

  public void execute() {
    List<Category> categories = categoryRepository.findAll();
    for (Category category : categories) {
      category.refreshPath();
    }
  }
}
