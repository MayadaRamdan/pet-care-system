package com.petcare.admin.zone.application;

import com.petcare.admin.zone.domain.Zone;
import com.petcare.admin.zone.repository.ZoneRepository;
import com.petcare.common.elasticsearch.dto.GeoPointDto;
import com.petcare.common.exception.domain.ResourceNotFoundException;
import com.petcare.common.geo.domain.Point;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class UpdateZoneCoordinatesUseCase {

  private final ZoneRepository zoneRepository;

  public void execute(Long id, List<GeoPointDto> coordinates) {
    Zone zone =
        zoneRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("zone", id));

    zone.setCoordinates(
        coordinates.stream()
            .map(gpd -> Point.of(gpd.lat(), gpd.lon()))
            .collect(Collectors.toList()));

    zoneRepository.save(zone);
  }
}
