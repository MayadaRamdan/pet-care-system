package com.petcare.common.elasticsearch.utils;

import com.petcare.common.geo.domain.Point;
import java.util.List;
import org.springframework.data.elasticsearch.core.geo.GeoJsonPolygon;

public class PolygonUtil {

  public static GeoJsonPolygon buildPolygon(List<Point> points) {
    if (points == null || points.isEmpty()) {
      throw new IllegalArgumentException("Points list cannot be null or empty");
    }

    // Convert GeoPoints to org.springframework.data.geo.Point for GeoJsonPolygon
    List<org.springframework.data.geo.Point> geoPoints =
        points.stream()
            .map(gp -> new org.springframework.data.geo.Point(gp.getLongitude(), gp.getLatitude()))
            .toList();

    // Ensure polygon is closed (first point equals last point)
    if (!geoPoints.getFirst().equals(geoPoints.getLast())) {
      geoPoints = new java.util.ArrayList<>(geoPoints);
      geoPoints.add(geoPoints.getFirst());
    }

    return GeoJsonPolygon.of(geoPoints);
  }
}
