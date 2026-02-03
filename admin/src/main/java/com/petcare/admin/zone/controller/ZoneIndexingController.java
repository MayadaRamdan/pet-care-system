package com.petcare.admin.zone.controller;

import com.petcare.admin.zone.application.IndexAllZonesUseCase;
import com.petcare.admin.zone.application.IndexOneZoneUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/zones/indecies")
@AllArgsConstructor
public class ZoneIndexingController {

  private final IndexAllZonesUseCase indexAllZonesUseCase;
  private final IndexOneZoneUseCase indexOneZoneUseCase;

  @PostMapping
  public ResponseEntity<Void> indexAll() {
    indexAllZonesUseCase.execute();
    return ResponseEntity.ok().build();
  }

  @PostMapping("{id}")
  public ResponseEntity<Void> indexOneZone(@PathVariable("id") Long id) {
    indexOneZoneUseCase.execute(id);
    return ResponseEntity.ok().build();
  }
}
