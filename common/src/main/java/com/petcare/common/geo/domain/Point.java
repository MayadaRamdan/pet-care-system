package com.petcare.common.geo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Point {

  @Column(name = "LATITUDE")
  private Double latitude;

  @Column(name = "LONGITUDE")
  private Double longitude;

  public static Point of(Double latitude, Double longitude) {
    return new Point(latitude, longitude);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Point other = (Point) o;
    boolean latitudeEquals =
        !(other.getLatitude() == null || getLatitude() == null)
            && Objects.equals(getLatitude(), other.getLatitude());
    boolean longitudeEquals =
        !(other.getLongitude() == null || getLongitude() == null)
            && Objects.equals(getLongitude(), other.getLongitude());
    return latitudeEquals && longitudeEquals;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(String.valueOf(getLatitude()).concat(String.valueOf(getLongitude())));
  }

  @Override
  public String toString() {
    return "(" + "lat=" + latitude + ", long=" + longitude + ')';
  }
}
