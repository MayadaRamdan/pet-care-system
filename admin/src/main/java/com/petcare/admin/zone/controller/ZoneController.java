package com.petcare.admin.zone.controller;

import com.petcare.admin.zone.application.CreateZoneUseCase;
import com.petcare.admin.zone.application.ListZonesUseCase;
import com.petcare.admin.zone.application.UpdateZoneCoordinatesUseCase;
import com.petcare.admin.zone.application.ZoneDetailsByIdUseCase;
import com.petcare.admin.zone.dto.CreateZoneRequest;
import com.petcare.admin.zone.dto.UpdateZoneCoordinatesRequest;
import com.petcare.admin.zone.dto.ZoneDetailsDto;
import com.petcare.admin.zone.dto.ZoneListingDto;
import com.petcare.common.common.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/zones")
@AllArgsConstructor
public class ZoneController {

  private final CreateZoneUseCase createZoneUseCase;
  private final ListZonesUseCase listZonesUseCase;
  private final ZoneDetailsByIdUseCase zoneDetailsByIdUseCase;
  private final UpdateZoneCoordinatesUseCase updateZoneCoordinatesUseCase;

  @PostMapping
  public ResponseEntity<ApiResponse> createZone(@RequestBody CreateZoneRequest request) {
    createZoneUseCase.execute(request);
    return ResponseEntity.ok(ApiResponse.success());
  }

  @GetMapping
  public ResponseEntity<Page<ZoneListingDto>> listZone(Pageable pageable) {
    return ResponseEntity.ok(listZonesUseCase.execute(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ZoneDetailsDto> detailsById(@PathVariable(name = "id") Long zoneId) {
    return ResponseEntity.ok(zoneDetailsByIdUseCase.execute(zoneId));
  }

  @PutMapping("/{id}/coordinates")
  public ResponseEntity<ApiResponse> updateZoneCoordinates(
      @PathVariable(name = "id") Long zoneId, @RequestBody UpdateZoneCoordinatesRequest request) {
    updateZoneCoordinatesUseCase.execute(zoneId, request.coordinates());
    return ResponseEntity.ok(ApiResponse.success());
  }
}
