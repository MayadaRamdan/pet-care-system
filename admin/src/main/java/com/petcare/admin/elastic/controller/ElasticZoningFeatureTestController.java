package com.petcare.admin.elastic.controller;

import com.petcare.common.elasticsearch.application.IndexZoneUseCase;
import com.petcare.common.elasticsearch.application.ZoneDocumentSearchUseCase;
import com.petcare.common.elasticsearch.domain.ZoneDocumentSearchResult;
import com.petcare.common.elasticsearch.dto.*;

import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/elastic-zone")
@AllArgsConstructor
public class ElasticZoningFeatureTestController {

  private final IndexZoneUseCase indexZoneUseCase;
  private final ZoneDocumentSearchUseCase zoneDocumentSearchUseCase;

  @PostMapping("/index/batch")
  public ResponseEntity<Void> indexZones(@RequestBody List<ZoneIndexRequest> requests) {
    requests.forEach(indexZoneUseCase::execute);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/search")
  public ResponseEntity<List<ZoneDocumentSearchResult>> searchZonesByPoint(
      @RequestBody GeoPointDto point) throws IOException {
    List<ZoneDocumentSearchResult> zones = zoneDocumentSearchUseCase.execute(point);
    return ResponseEntity.ok(zones);
  }
}
