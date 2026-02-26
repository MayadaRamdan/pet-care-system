package com.petcare.common.catalog.domain;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ItemAssetPK implements Serializable {

  private long itemId;
  private long assetId;

  @Override
  public int hashCode() {
    return Objects.hash(itemId, assetId);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ItemAssetPK other)) {
      return false;
    }
    return this.itemId == other.itemId && this.assetId == other.assetId;
  }
}
