package com.petcare.admin.catalog.category.controller;

import com.petcare.admin.catalog.category.application.CategoryDetailsByIdUseCase;
import com.petcare.admin.catalog.category.application.CreateCategoryUseCase;
import com.petcare.admin.catalog.category.application.ListAllCategoriesUseCase;
import com.petcare.admin.catalog.category.application.ListCategoriesByParentIdUseCase;
import com.petcare.admin.catalog.category.application.UpdateCategoryThumbnailUseCase;
import com.petcare.admin.catalog.category.dto.CreateCategoryRequest;
import com.petcare.common.common.response.ApiResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
public class CategoryController {

  private final CreateCategoryUseCase createCategoryUseCase;
  private final ListAllCategoriesUseCase listAllCategoriesUseCase;
  private final ListCategoriesByParentIdUseCase listCategoriesByParentIdUseCase;
  private final UpdateCategoryThumbnailUseCase updateCategoryThumbnailUseCase;
  private final CategoryDetailsByIdUseCase categoryDetailsByIdUseCase;

  @PostMapping
  public ResponseEntity<Void> createCategory(@RequestBody CreateCategoryRequest request) {
    createCategoryUseCase.execute(request);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<ApiResponse> listAllCategories() {
    return ResponseEntity.ok(ApiResponse.success(listAllCategoriesUseCase.execute()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getCategoryDetailsById(
      @PathVariable(name = "id") Long categoryId) {
    return ResponseEntity.ok(ApiResponse.success(categoryDetailsByIdUseCase.execute(categoryId)));
  }

  @GetMapping("/{id}/children")
  public ResponseEntity<ApiResponse> listCategoryChildren(
      @PathVariable(name = "id") Long parentId) {
    return ResponseEntity.ok(
        ApiResponse.success(listCategoriesByParentIdUseCase.execute(parentId)));
  }

  @PostMapping("/{id}/assets")
  public ResponseEntity<Void> uploadThumbnail(
      @PathVariable(name = "id") Long categoryId,
      @RequestParam(name = "file", required = false) MultipartFile file)
      throws IOException {
    updateCategoryThumbnailUseCase.execute(categoryId, file);
    return ResponseEntity.ok().build();
  }
}
