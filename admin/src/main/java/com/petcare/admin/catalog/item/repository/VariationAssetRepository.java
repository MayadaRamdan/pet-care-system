package com.petcare.admin.catalog.item.repository;

import com.petcare.admin.catalog.item.domain.VariationAsset;
import com.petcare.common.catalog.domain.VariationAssetPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface VariationAssetRepository extends JpaRepository<VariationAsset, VariationAssetPK> {

  @Modifying(clearAutomatically = true)
  @Query(value = "delete from VariationAsset va where va.variationId = :variationId and va.assetId in :assetsIds")
  void deleteByVariationIdAndAssetsIdsIn(Long variationId, Set<Long> assetsIds);
}
