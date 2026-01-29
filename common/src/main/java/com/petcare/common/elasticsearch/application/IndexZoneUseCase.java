package com.petcare.common.elasticsearch.application;

import com.petcare.common.elasticsearch.domain.ZoneDocument;
import com.petcare.common.elasticsearch.dto.ZoneIndexRequest;
import com.petcare.common.elasticsearch.repository.ZoneDocumentRepository;
import com.petcare.common.elasticsearch.utils.PolygonUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.core.geo.GeoJsonPolygon;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IndexZoneUseCase {

  private final ZoneDocumentRepository zoneDocumentRepository;

  public void execute(ZoneIndexRequest zoneRequest) {

    if (zoneRequest.name() == null || zoneRequest.name().isBlank()) {
      throw new IllegalArgumentException("Zone name is required");
    }
    if (zoneRequest.code() == null || zoneRequest.code().isBlank()) {
      throw new IllegalArgumentException("Zone code is required");
    }
    if (zoneRequest.polygonPoints() == null || zoneRequest.polygonPoints().size() < 3) {
      throw new IllegalArgumentException("At least 3 points are required to form a polygon");
    }

    GeoJsonPolygon geoJsonPolygon = PolygonUtil.buildPolygon(zoneRequest.polygonPoints());

    ZoneDocument document =
        zoneDocumentRepository.findById(zoneRequest.id()).orElse(new ZoneDocument());

    document.setId(zoneRequest.id());
    document.setName(zoneRequest.name());
    document.setCode(zoneRequest.code());
    document.setStatus(zoneRequest.status());
    document.setPolygon(geoJsonPolygon);

    zoneDocumentRepository.save(document);
  }
}
