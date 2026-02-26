package com.petcare.admin.catalog.item.domain;

import com.petcare.common.asset.domain.Asset;
import com.petcare.common.catalog.domain.ItemAssetPK;
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
@IdClass(ItemAssetPK.class)
@Entity
@Table(name = "item_asset")
public class ItemAsset {

  @Id
  @Column(name = "item_id")
  private long itemId;

  @Id
  @Column(name = "asset_id")
  private long assetId;

  private boolean main;

  @ManyToOne
  @MapsId("itemId")
  @JoinColumn(name = "item_id")
  private Item item;

  @ManyToOne
  @MapsId("assetId")
  @JoinColumn(name = "asset_id")
  private Asset asset;

  public static ItemAsset of(Item item, Asset asset, Boolean main) {
    ItemAsset ci = new ItemAsset();
    ci.setItem(item);
    ci.setItemId(item.getId());
    ci.setAsset(asset);
    ci.setAssetId(asset.getId());
    ci.setMain((main == null) ? Boolean.FALSE : main);
    return ci;
  }
}
