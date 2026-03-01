package com.petcare.admin.zone.application;

import com.petcare.admin.zone.domain.Zone;
import com.petcare.admin.zone.repository.ZoneRepository;
import com.petcare.common.elasticsearch.application.IndexZoneUseCase;
import com.petcare.common.elasticsearch.dto.GeoPointDto;
import com.petcare.common.elasticsearch.dto.ZoneIndexRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class IndexAllZonesUseCase {

  private final ZoneRepository zoneRepository;
  private final IndexZoneUseCase indexZoneUseCase;

  @Async
  public void execute() {
    zoneRepository.findNotDeleted().forEach(z -> indexZoneUseCase.execute(map(z)));
  }

  private ZoneIndexRequest map(Zone z) {
    List<GeoPointDto> points =
        z.getCoordinates().stream().map(p -> new GeoPointDto(p.getLat(), p.getLon())).toList();

    return new ZoneIndexRequest(
        z.getId(), z.getCode(), z.getName().getEnglish(), z.getStatus().name(), points);
  }
}
