package com.petcare.customer.catalog.controller;

import com.petcare.common.common.response.ApiResponse;
import com.petcare.customer.catalog.application.GetCategoriesTreeUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
public class CategoryController {

  private final GetCategoriesTreeUseCase getCategoriesTreeUseCase;

  @GetMapping("/tree")
  public ResponseEntity<ApiResponse> tree() {
    return ResponseEntity.ok(ApiResponse.success(getCategoriesTreeUseCase.execute()));
  }
}
