package com.petcare.admin.zonemerchant.controller;

import com.petcare.admin.zonemerchant.application.UpdateZoneStoresUseCase;
import com.petcare.admin.zonemerchant.dto.UpdateZoneStoresRequest;
import com.petcare.common.common.response.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/zone-merchants")
@AllArgsConstructor
public class ZoneMerchantController {

  private final UpdateZoneStoresUseCase updateZoneStoresUseCase;

  @PutMapping
  public ResponseEntity<ApiResponse> updateZoneStores(
      @RequestBody UpdateZoneStoresRequest request) {
    updateZoneStoresUseCase.execute(request);
    return ResponseEntity.ok(ApiResponse.success());
  }
}
