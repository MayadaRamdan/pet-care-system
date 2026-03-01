package com.petcare.admin.catalog.item.repository;

import com.petcare.admin.catalog.item.domain.Item;
import com.petcare.admin.catalog.item.domain.ItemStatus;
import com.petcare.common.asset.domain.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

  @Modifying(clearAutomatically = true)
  @Query(
      value =
          "update Item it set it.thumbnail= :thumbnail, it.thumbnailUrl = :thumbnailUrl where it.id = :id")
  void updateThumbnail(Long id, Asset thumbnail, String thumbnailUrl);

  @Modifying(clearAutomatically = true)
  @Query("update Item it set it.status = :status where it.id =:itemId")
  void updateStatusById(Long itemId, ItemStatus status);
}
