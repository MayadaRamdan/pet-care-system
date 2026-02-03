package com.petcare.admin.zone.dto;

import com.petcare.admin.zone.domain.ZoneStatus;
import com.petcare.common.common.dto.IdName;
import com.petcare.common.common.embeddable.LocalizableString;
import com.petcare.common.elasticsearch.dto.GeoPointDto;
import java.util.List;

public record ZoneDetailsDto(
    Long id,
    String code,
    LocalizableString name,
    ZoneStatus status,
    List<GeoPointDto> coordinates,
    List<IdName> merchants) {}
