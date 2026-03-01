package com.petcare.common.elasticsearch.utils;

import com.petcare.common.elasticsearch.dto.GeoPointDto;
import java.util.List;
import org.springframework.data.elasticsearch.core.geo.GeoJsonPolygon;
import org.springframework.data.geo.Point;

public class PolygonUtil {

  public static GeoJsonPolygon buildPolygon(List<GeoPointDto> points) {
    if (points == null || points.isEmpty()) {
      throw new IllegalArgumentException("Points list cannot be null or empty");
    }

    // Convert GeoPoints to org.springframework.data.geo.Point for GeoJsonPolygon
    List<Point> geoPoints = points.stream().map(gp -> new Point(gp.lon(), gp.lat())).toList();

    // Ensure polygon is closed (first point equals last point)
    if (!geoPoints.getFirst().equals(geoPoints.getLast())) {
      geoPoints = new java.util.ArrayList<>(geoPoints);
      geoPoints.add(geoPoints.getFirst());
    }

    return GeoJsonPolygon.of(geoPoints);
  }
}
