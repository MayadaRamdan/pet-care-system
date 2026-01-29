package com.petcare.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.petcare.admin.catalog.category.application.CreateCategoryUseCase;
import com.petcare.admin.catalog.category.domain.Category;
import com.petcare.admin.catalog.category.domain.CategoryStatus;
import com.petcare.admin.catalog.category.dto.CreateCategoryRequest;
import com.petcare.admin.catalog.category.repository.CategoryRepository;
import com.petcare.common.common.embeddable.LocalizableString;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateCategoryUseCaseTest {

  @Mock private CategoryRepository categoryRepository;

  @InjectMocks private CreateCategoryUseCase createCategoryUseCase;

  @Test
  @DisplayName("Should create a root category (level 0) when parentId is null")
  void shouldCreateRootCategory() {
    // Given
    LocalizableString name = LocalizableString.of("Food", "طعام");
    CreateCategoryRequest request = new CreateCategoryRequest(name, null);

    // When
    createCategoryUseCase.execute(request);

    // Then
    ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
    verify(categoryRepository).save(categoryCaptor.capture());

    Category savedCategory = categoryCaptor.getValue();
    assertThat(savedCategory.getLevel()).isZero();
    assertThat(savedCategory.getName()).isEqualTo(name);
    assertThat(savedCategory.getParent()).isNull();
    assertThat(savedCategory.getStatus()).isEqualTo(CategoryStatus.INACTIVE);
  }

  @Test
  @DisplayName("Should create a child category with level = parent.level + 1")
  void shouldCreateChildCategory() {
    // Given
    Long parentId = 1L;
    Category parent = new Category();
    parent.setId(parentId);
    parent.setLevel(1);

    CreateCategoryRequest request =
        new CreateCategoryRequest(LocalizableString.of("Dog Food", "طعام كلاب"), parentId);

    when(categoryRepository.findById(parentId)).thenReturn(Optional.of(parent));

    // When
    createCategoryUseCase.execute(request);

    // Then
    ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
    verify(categoryRepository).save(categoryCaptor.capture());

    Category savedCategory = categoryCaptor.getValue();
    assertThat(savedCategory.getParent()).isEqualTo(parent);
    assertThat(savedCategory.getLevel()).isEqualTo(2);
  }

  @Test
  @DisplayName("Should throw exception when parentId is provided but parent does not exist")
  void shouldThrowExceptionWhenParentNotFound() {
    // Given
    Long parentId = 99L;
    CreateCategoryRequest request = new CreateCategoryRequest(null, parentId);

    when(categoryRepository.findById(parentId)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(RuntimeException.class, () -> createCategoryUseCase.execute(request));
    verify(categoryRepository, never()).save(any());
  }
}
