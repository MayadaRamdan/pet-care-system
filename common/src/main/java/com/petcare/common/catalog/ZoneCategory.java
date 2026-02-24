package com.petcare.common.catalog;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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

  public static ZoneCategory of(Long zoneId, Long categoryId, int count) {
    ZoneCategory zc = new ZoneCategory();
    zc.setZoneId(zoneId);
    zc.setCategoryId(categoryId);
    zc.setPublishedItemsCount(count);
    return zc;
  }
}
