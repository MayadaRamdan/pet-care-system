package com.petcare.common.catalog.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class StockDetails {

  @Enumerated(EnumType.STRING)
  private StockMode stockMode;

  @ManyToOne private Stock stock;

  private Integer unitCapacity;

  private Boolean hideWhenOutOfStock = Boolean.FALSE;

  public Integer getStockQty() {
    return stock.getQuantity() / unitCapacity;
  }

  public int getUnitCapacity() {
    if (this.unitCapacity == null) return 1;
    return this.unitCapacity;
  }
}
