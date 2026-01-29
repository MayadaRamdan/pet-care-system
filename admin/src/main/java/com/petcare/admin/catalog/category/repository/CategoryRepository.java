package com.petcare.admin.catalog.category.repository;

import com.petcare.admin.catalog.category.domain.Category;
import com.petcare.admin.catalog.category.domain.CategoryListing;
import com.petcare.admin.catalog.category.dto.CategoryListNode;
import com.petcare.common.asset.domain.Asset;
import com.petcare.common.common.dto.IdName;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  @Query(
      """
          select c.id as id, c.name.english as name, c.status as status
          from Category c where c.parent.id = :parentId and c.deleted = false
          order by c.displayOrder
          """)
  List<CategoryListing> findSortedByParentId(Long parentId);

  @Modifying(clearAutomatically = true)
  @Query(
      value =
          "update Category c set c.thumbnail= :thumbnail, c.thumbnailUrl = :thumbnailUrl where c.id = :id")
  void updateThumbnail(Long id, Asset thumbnail, String thumbnailUrl);

  @Query(
      """
          select new com.petcare.admin.catalog.category.dto.CategoryListNode(
          c.id, c.name.english, c.path.english, c.parent.id)
          from Category c where c.deleted = false
          """)
  List<CategoryListNode> listAllCategories();

  @Query(
      """
          select new com.petcare.common.common.dto.IdName(c.id, c.path.english)
          from Category c where c.id in :ids
          """)
  List<IdName> listCategoriesByIdIn(Set<Long> ids);
}
