package com.petcare.common.zonemerchant;

import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ZoneMerchantPK implements Serializable {

  @Id private long zoneId;

  @Id private long merchantId;

  @Override
  public int hashCode() {
    return Objects.hash(zoneId, merchantId);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ZoneMerchantPK other)) {
      return false;
    }
    return this.zoneId == other.zoneId && this.merchantId == other.merchantId;
  }
}
