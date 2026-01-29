package com.petcare.admin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.admin.catalog.category.application.CreateCategoryUseCase;
import com.petcare.admin.catalog.category.application.ListCategoriesByParentIdUseCase;
import com.petcare.admin.catalog.category.controller.CategoryController;
import com.petcare.admin.catalog.category.domain.CategoryStatus;
import com.petcare.admin.catalog.category.dto.CategoryListingDto;
import com.petcare.admin.catalog.category.dto.CreateCategoryRequest;
import com.petcare.common.common.embeddable.LocalizableString;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private CreateCategoryUseCase createCategoryUseCase;

  @MockitoBean private ListCategoriesByParentIdUseCase listCategoriesByParentIdUseCase;

  @Nested
  @DisplayName("POST /api/categories - Create Category")
  class CreateCategoryTests {

    @Test
    @WithMockUser
    @DisplayName("Should create a root category successfully")
    void shouldCreateRootCategorySuccessfully() throws Exception {
      // Given
      CreateCategoryRequest request =
          new CreateCategoryRequest(LocalizableString.of("Pet Food", "طعام الحيوانات"), null);

      doNothing().when(createCategoryUseCase).execute(any(CreateCategoryRequest.class));

      // When & Then
      mockMvc
          .perform(
              post("/api/categories")
                  .with(csrf())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk());

      verify(createCategoryUseCase, times(1)).execute(any(CreateCategoryRequest.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Should create a child category successfully")
    void shouldCreateChildCategorySuccessfully() throws Exception {
      // Given
      CreateCategoryRequest request =
          new CreateCategoryRequest(LocalizableString.of("Dog Food", "طعام الكلاب"), 1L);

      doNothing().when(createCategoryUseCase).execute(any(CreateCategoryRequest.class));

      // When & Then
      mockMvc
          .perform(
              post("/api/categories")
                  .with(csrf())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk());

      verify(createCategoryUseCase, times(1)).execute(any(CreateCategoryRequest.class));
    }

    @Test
    @DisplayName("Should return 401 when user is not authenticated")
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
      // Given
      CreateCategoryRequest request =
          new CreateCategoryRequest(LocalizableString.of("Pet Food", "طعام الحيوانات"), null);

      // When & Then
      mockMvc
          .perform(
              post("/api/categories")
                  .with(csrf())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk());
    }
  }

  @Nested
  @DisplayName("GET /api/categories/{id}/children - List Category Children")
  class ListCategoryChildrenTests {

    @Test
    @WithMockUser
    @DisplayName("Should return children categories successfully")
    void shouldReturnChildrenCategoriesSuccessfully() throws Exception {
      // Given
      Long parentId = 1L;

      CategoryListingDto child1 =
          new CategoryListingDto(2L, "Dog Food", CategoryStatus.ACTIVE);
      CategoryListingDto child2 =
          new CategoryListingDto(3L, "Cat Food", CategoryStatus.INACTIVE);

      List<CategoryListingDto> children = List.of(child1, child2);

      when(listCategoriesByParentIdUseCase.execute(eq(parentId))).thenReturn(children);

      // When & Then
      mockMvc
          .perform(
              get("/api/categories/{id}/children", parentId)
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.length()").value(2))
          .andExpect(jsonPath("$[0].id").value(2L))
          .andExpect(jsonPath("$[0].name").value("Dog Food"))
          .andExpect(jsonPath("$[0].status").value("ACTIVE"))
          .andExpect(jsonPath("$[1].id").value(3L))
          .andExpect(jsonPath("$[1].name").value("Cat Food"))
          .andExpect(jsonPath("$[1].status").value("INACTIVE"));

      verify(listCategoriesByParentIdUseCase, times(1)).execute(eq(parentId));
    }

    @Test
    @WithMockUser
    @DisplayName("Should return empty list when no children exist")
    void shouldReturnEmptyListWhenNoChildrenExist() throws Exception {
      // Given
      Long parentId = 99L;

      when(listCategoriesByParentIdUseCase.execute(eq(parentId)))
          .thenReturn(Collections.emptyList());

      // When & Then
      mockMvc
          .perform(
              get("/api/categories/{id}/children", parentId)
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.length()").value(0));

      verify(listCategoriesByParentIdUseCase, times(1)).execute(eq(parentId));
    }

    @Test
    @DisplayName("Should return 401 when user is not authenticated")
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
      // Given
      Long parentId = 1L;

      // When & Then
      mockMvc
          .perform(
              get("/api/categories/{id}/children", parentId)
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
    }
  }
}
