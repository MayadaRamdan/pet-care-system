package com.petcare.admin.catalog.item.repository;

import com.petcare.admin.catalog.item.domain.CategoryItem;
import com.petcare.common.catalog.domain.CategoryItemPK;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryItemRepository extends JpaRepository<CategoryItem, CategoryItemPK> {

  @Modifying(clearAutomatically = true)
  @Query(
      value =
          "delete from CategoryItem ci where ci.itemId = :itemId and ci.categoryId in :categoriesIds")
  void deleteByItemIdAndCategoriesIdsIn(Long itemId, Set<Long> categoriesIds);
}
