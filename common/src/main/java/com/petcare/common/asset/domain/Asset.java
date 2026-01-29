package com.petcare.common.asset.domain;

import com.petcare.common.common.domain.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Asset extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "asset_url")
  private String url;

  @Enumerated(EnumType.STRING)
  @Column(name = "url_mode")
  private AssetUrlMode urlMode;

  public static Asset relativeOf(String url) {
    Asset asset = new Asset();
    asset.setUrl(url);
    asset.setUrlMode(AssetUrlMode.RELATIVE);
    return asset;
  }

  public static Asset absoluteOf(String url) {
    Asset asset = new Asset();
    asset.setUrl(url);
    asset.setUrlMode(AssetUrlMode.ABSOLUTE);
    return asset;
  }
}
