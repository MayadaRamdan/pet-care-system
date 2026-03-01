package com.petcare.admin.zonemerchant.controller;

import com.petcare.admin.zonemerchant.application.GetZoneMerchantsUseCase;
import com.petcare.admin.zonemerchant.application.UpdateZoneMerchantsUseCase;
import com.petcare.admin.zonemerchant.dto.UpdateZoneMerchantRequest;
import com.petcare.common.common.response.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/zones")
@AllArgsConstructor
public class ZoneMerchantController {

  private final GetZoneMerchantsUseCase getZoneMerchantsUseCase;
  private final UpdateZoneMerchantsUseCase updateZoneMerchantsUseCase;

  @GetMapping("/{zoneId}/merchants")
  public ResponseEntity<ApiResponse> getZoneStores(@PathVariable("zoneId") Long zoneId) {
    return ResponseEntity.ok(ApiResponse.success(getZoneMerchantsUseCase.execute(zoneId)));
  }

  @PutMapping("/merchants")
  public ResponseEntity<ApiResponse> updateZoneStores(
      @RequestBody UpdateZoneMerchantRequest request) {
    updateZoneMerchantsUseCase.execute(request);
    return ResponseEntity.ok(ApiResponse.success());
  }
}
