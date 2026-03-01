package com.petcare.admin.zone.dto;

import com.petcare.common.elasticsearch.dto.GeoPointDto;
import java.util.List;

public record UpdateZoneCoordinatesRequest(List<GeoPointDto> coordinates) {}
