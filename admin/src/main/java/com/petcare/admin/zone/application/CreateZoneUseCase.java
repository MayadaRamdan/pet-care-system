package com.petcare.admin.zone.application;

import com.petcare.admin.zone.domain.Zone;
import com.petcare.admin.zone.domain.ZoneStatus;
import com.petcare.admin.zone.dto.CreateZoneRequest;
import com.petcare.admin.zone.repository.ZoneRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class CreateZoneUseCase {

  private final ZoneRepository zoneRepository;

  public void execute(CreateZoneRequest request) {
    Zone z = new Zone();
    z.setCode(request.code());
    z.setName(request.name());
    z.setCoordinates(request.coordinates());
    z.setStatus(ZoneStatus.INACTIVE);
    zoneRepository.save(z);
  }
}
