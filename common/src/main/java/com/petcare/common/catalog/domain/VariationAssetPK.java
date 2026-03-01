package com.petcare.common.catalog.domain;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class VariationAssetPK implements Serializable {

  private long variationId;
  private long assetId;

  @Override
  public int hashCode() {
    return Objects.hash(variationId, assetId);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof VariationAssetPK other)) {
      return false;
    }
    return this.variationId == other.variationId && this.assetId == other.assetId;
  }
}
