package com.petcare.admin.zonemerchant.domain;

import com.petcare.admin.merchant.domain.Merchant;
import com.petcare.admin.zone.domain.Zone;
import com.petcare.common.zonemerchant.ZoneMerchantPK;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@IdClass(ZoneMerchantPK.class)
@Table(name = "zone_merchant")
public class ZoneMerchant {

  @Id private long zoneId;
  @Id private long merchantId;

  @ManyToOne
  @MapsId("zoneId")
  @JoinColumn(name = "zone_id")
  private Zone zone;

  @ManyToOne
  @MapsId("merchantId")
  @JoinColumn(name = "merchant_id")
  private Merchant merchant;

  public static ZoneMerchant of(Zone zone, Merchant merchant) {
    ZoneMerchant zm = new ZoneMerchant();
    zm.setZone(zone);
    zm.setZoneId(zone.getId());
    zm.setMerchant(merchant);
    zm.setMerchantId(merchant.getId());
    return zm;
  }
}
