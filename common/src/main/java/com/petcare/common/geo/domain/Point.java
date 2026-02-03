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

  @Column(name = "lat")
  private Double lat;

  @Column(name = "lon")
  private Double lon;

  public static Point of(Double lat, Double lon) {
    return new Point(lat, lon);
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
    boolean latEquals =
        !(other.getLat() == null || getLat() == null) && Objects.equals(getLat(), other.getLat());
    boolean lonEquals =
        !(other.getLon() == null || getLon() == null) && Objects.equals(getLon(), other.getLon());
    return latEquals && lonEquals;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(String.valueOf(getLat()).concat(String.valueOf(getLon())));
  }

  @Override
  public String toString() {
    return "(" + "lat=" + lat + ", long=" + lon + ')';
  }
}
