package com.petcare.customer.catalog.domain;

import com.petcare.common.catalog.ZoneCategoryPK;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@IdClass(ZoneCategoryPK.class)
@Entity
@Table(name = "zone_category")
public class ZoneCategory {

  @Id private long zoneId;

  @Id private long categoryId;

  private int publishedItemsCount;

  @ManyToOne
  @MapsId("categoryId")
  @JoinColumn(name = "category_id")
  private Category category;

  public static ZoneCategory of(Long zoneId, Long categoryId, int count) {
    ZoneCategory zc = new ZoneCategory();
    zc.setZoneId(zoneId);
    zc.setCategoryId(categoryId);
    zc.setPublishedItemsCount(count);
    return zc;
  }
}
