package com.petcare.admin.zone.application;

import com.petcare.admin.zone.domain.Zone;
import com.petcare.admin.zone.repository.ZoneRepository;
import com.petcare.common.elasticsearch.application.IndexZoneUseCase;
import com.petcare.common.elasticsearch.dto.GeoPointDto;
import com.petcare.common.elasticsearch.dto.ZoneIndexRequest;
import com.petcare.common.exception.domain.ResourceNotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class IndexOneZoneUseCase {

  private final ZoneRepository zoneRepository;
  private final IndexZoneUseCase indexZoneUseCase;

  public void execute(Long zoneId) {
    Zone zone =
        zoneRepository
            .findById(zoneId)
            .orElseThrow(() -> new ResourceNotFoundException("zone", zoneId));
    indexZoneUseCase.execute(map(zone));
  }

  private ZoneIndexRequest map(Zone z) {
    List<GeoPointDto> points =
        z.getCoordinates().stream().map(p -> new GeoPointDto(p.getLat(), p.getLon())).toList();

    return new ZoneIndexRequest(
        z.getId(), z.getCode(), z.getName().getEnglish(), z.getStatus().name(), points);
  }
}
