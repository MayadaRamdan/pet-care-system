package com.petcare.admin.merchant.controller;

import com.petcare.admin.merchant.application.CreateMerchantUseCase;
import com.petcare.admin.merchant.application.ListMerchantsUseCase;
import com.petcare.admin.merchant.dto.CreateMerchantRequest;
import com.petcare.admin.merchant.dto.MerchantListingRecord;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/merchants")
@AllArgsConstructor
public class MerchantController {

  private final CreateMerchantUseCase createMerchantUseCase;
  private final ListMerchantsUseCase listMerchantsUseCase;

  @PostMapping
  public ResponseEntity<Void> createMerchant(@RequestBody CreateMerchantRequest request) {
    createMerchantUseCase.execute(request);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<List<MerchantListingRecord>> listMerchants() {
    return ResponseEntity.ok(listMerchantsUseCase.execute());
  }
}
