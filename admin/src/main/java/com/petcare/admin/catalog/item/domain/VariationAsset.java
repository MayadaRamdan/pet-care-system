package com.petcare.admin.catalog.item.domain;

import com.petcare.common.asset.domain.Asset;
import com.petcare.common.catalog.domain.VariationAssetPK;
import jakarta.persistence.Column;
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
@IdClass(VariationAssetPK.class)
@Entity
@Table(name = "variation_asset")
public class VariationAsset {

  @Id
  @Column(name = "variation_id")
  private long variationId;

  @Id
  @Column(name = "asset_id")
  private long assetId;

  private boolean main;

  @ManyToOne
  @MapsId("variationId")
  @JoinColumn(name = "variation_id")
  private Variation variation;

  @ManyToOne
  @MapsId("assetId")
  @JoinColumn(name = "asset_id")
  private Asset asset;

  public static VariationAsset of(Variation variation, Asset asset, Boolean main) {
    VariationAsset ci = new VariationAsset();
    ci.setVariation(variation);
    ci.setVariationId(variation.getId());
    ci.setAsset(asset);
    ci.setAssetId(asset.getId());
    ci.setMain((main == null) ? Boolean.FALSE : main);
    return ci;
  }
}
