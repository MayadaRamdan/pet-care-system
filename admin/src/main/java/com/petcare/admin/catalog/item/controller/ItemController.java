package com.petcare.admin.catalog.item.controller;

import com.petcare.admin.catalog.item.application.CreateItemUseCase;
import com.petcare.admin.catalog.item.application.ItemDetailsUseCase;
import com.petcare.admin.catalog.item.application.SyncItemVariationsUseCase;
import com.petcare.admin.catalog.item.application.UpdateItemThumbnailUseCase;
import com.petcare.admin.catalog.item.application.UpdateItemUseCase;
import com.petcare.admin.catalog.item.dto.ItemDetailsDto;
import com.petcare.admin.catalog.item.dto.ItemVariations;
import com.petcare.common.common.response.ApiResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/items")
@AllArgsConstructor
public class ItemController {

  private final CreateItemUseCase createItemUseCase;
  private final UpdateItemUseCase updateItemUseCase;
  private final ItemDetailsUseCase itemDetailsUseCase;
  private final UpdateItemThumbnailUseCase updateItemThumbnailUseCase;
  private final SyncItemVariationsUseCase syncItemVariationsUseCase;

  @PostMapping
  public ResponseEntity<Void> createItem(@RequestBody ItemDetailsDto request) {
    createItemUseCase.execute(request);
    return ResponseEntity.ok().build();
  }

  @PutMapping
  public ResponseEntity<Void> updateItem(@RequestBody ItemDetailsDto request) {
    updateItemUseCase.execute(request);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getItemDetails(@PathVariable(name = "id") Long itemId) {
    return ResponseEntity.ok(ApiResponse.success(itemDetailsUseCase.execute(itemId)));
  }

  @PostMapping("/{id}/assets")
  public ResponseEntity<Void> uploadThumbnail(
      @PathVariable(name = "id") Long itemId,
      @RequestParam(name = "file", required = false) MultipartFile file)
      throws IOException {
    updateItemThumbnailUseCase.execute(itemId, file);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}/variations")
  public ResponseEntity<Void> updateItemVariations(
      @PathVariable(name = "id") Long itemId, @RequestBody ItemVariations request) {
    syncItemVariationsUseCase.execute(itemId, request.variations());
    return ResponseEntity.ok().build();
  }
}
