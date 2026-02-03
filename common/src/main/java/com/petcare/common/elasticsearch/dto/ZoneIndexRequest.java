package com.petcare.common.elasticsearch.dto;

import java.util.List;

public record ZoneIndexRequest(
    Long id, String name, String code, String status, List<GeoPointDto> polygonPoints) {}
