package com.petcare.common.catalog.domain;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CategoryAncestorDescendantPK implements Serializable {

  private long ancestorId;
  private long descendantId;

  @Override
  public int hashCode() {
    return Objects.hash(ancestorId, descendantId);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof CategoryAncestorDescendantPK other)) {
      return false;
    }
    return this.ancestorId == other.ancestorId && this.descendantId == other.descendantId;
  }
}
