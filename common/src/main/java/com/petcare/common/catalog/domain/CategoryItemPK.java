package com.petcare.common.catalog.domain;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CategoryItemPK implements Serializable {

  private long categoryId;
  private long itemId;

  @Override
  public int hashCode() {
    return Objects.hash(categoryId, itemId);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof CategoryItemPK other)) {
      return false;
    }
    return this.categoryId == other.categoryId && this.itemId == other.itemId;
  }
}
