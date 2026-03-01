package com.petcare.admin.zone.application;

import com.petcare.admin.zone.dto.ZoneListingDto;
import com.petcare.admin.zone.repository.ZoneRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ListZonesUseCase {

  private final ZoneRepository zoneRepository;

  public Page<ZoneListingDto> execute(Pageable pageable) {
    return zoneRepository
        .listZones(pageable)
        .map(z -> new ZoneListingDto(z.getId(), z.getCode(), z.getName(), z.getStatus()));
  }
}
