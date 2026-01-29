package com.petcare.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.petcare.admin.catalog.category.application.ListCategoriesByParentIdUseCase;
import com.petcare.admin.catalog.category.domain.CategoryListing;
import com.petcare.admin.catalog.category.domain.CategoryStatus;
import com.petcare.admin.catalog.category.dto.CategoryListingDto;
import com.petcare.admin.catalog.category.repository.CategoryRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListCategoriesByParentIdUseCaseTest {

  @Mock private CategoryRepository categoryRepository;

  @InjectMocks private ListCategoriesByParentIdUseCase listCategoriesByParentIdUseCase;

  @Test
  @DisplayName("Should map CategoryListing projection to CategoryListingDto")
  void shouldReturnMappedDtos() {
    // Given
    Long parentId = 1L;

    // Mocking the projection interface
    CategoryListing mockListing =
        new CategoryListing() {
          @Override
          public Long getId() {
            return 10L;
          }

          @Override
          public String getName() {
            return "Dry Food";
          }

          @Override
          public CategoryStatus getStatus() {
            return CategoryStatus.ACTIVE;
          }
        };

    when(categoryRepository.findSortedByParentId(parentId)).thenReturn(List.of(mockListing));

    // When
    List<CategoryListingDto> result = listCategoriesByParentIdUseCase.execute(parentId);

    // Then
    assertThat(result).hasSize(1);
    CategoryListingDto dto = result.get(0);
    assertThat(dto.id()).isEqualTo(10L);
    assertThat(dto.name()).isEqualTo("Dry Food");
    assertThat(dto.status()).isEqualTo(CategoryStatus.ACTIVE);
  }
}
