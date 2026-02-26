package com.petcare.admin.catalog.item.repository;

import com.petcare.admin.catalog.item.domain.ItemAsset;
import com.petcare.common.catalog.domain.ItemAssetPK;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemAssetRepository extends JpaRepository<ItemAsset, ItemAssetPK> {

    @Modifying(clearAutomatically = true)
    @Query(
            value =
                    "delete from ItemAsset ia where ia.itemId = :itemId and ia.assetId in :assetsIds")
    void deleteByItemIdAndAssetsIdsIn(Long itemId, Set<Long> assetsIds);
}
