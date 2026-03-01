package com.petcare.common.catalog.domain;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ZoneCategoryPK implements Serializable {

  private long zoneId;
  private long categoryId;

  @Override
  public int hashCode() {
    return Objects.hash(zoneId, categoryId);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ZoneCategoryPK other)) {
      return false;
    }

    return this.zoneId == other.zoneId && this.categoryId == other.categoryId;
  }
}
