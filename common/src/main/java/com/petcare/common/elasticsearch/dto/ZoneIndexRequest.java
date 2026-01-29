package com.petcare.common.elasticsearch.dto;

import com.petcare.common.geo.domain.Point;
import java.util.List;

public record ZoneIndexRequest(
    Long id, String name, String code, String status, List<Point> polygonPoints) {}
