package com.petcare.admin.zone.application;

import com.petcare.admin.zone.domain.Zone;
import com.petcare.admin.zone.dto.ZoneDetailsDto;
import com.petcare.admin.zone.repository.ZoneRepository;
import com.petcare.common.common.dto.IdName;
import com.petcare.common.elasticsearch.dto.GeoPointDto;
import com.petcare.common.exception.domain.ResourceNotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ZoneDetailsByIdUseCase {

  private final ZoneRepository zoneRepository;

  public ZoneDetailsDto execute(Long id) {
    Zone zone =
        zoneRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("zone", id));

    List<GeoPointDto> points =
        zone.getCoordinates().stream().map(p -> new GeoPointDto(p.getLat(), p.getLon())).toList();

    List<IdName> zoneMerchants = zoneRepository.findZoneMerchants(id);
    return new ZoneDetailsDto(
        zone.getId(), zone.getCode(), zone.getName(), zone.getStatus(), points, zoneMerchants);
  }
}
