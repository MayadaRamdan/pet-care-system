package com.petcare.customer.catalog.repository;

import com.petcare.customer.catalog.domain.Category;
import com.petcare.customer.catalog.domain.CategoryFlatRow;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  @Query(
      """
              select new com.petcare.customer.catalog.domain.CategoryFlatRow(
              c.id, c.name, c.path, c.thumbnailUrl, c.parent.id, c.displayOrder)
              from Category c
              """)
  List<CategoryFlatRow> listAllForTree();

  @Query(
      """
              select new com.petcare.customer.catalog.domain.CategoryFlatRow(
              c.id, c.name, c.path, c.thumbnailUrl, c.parent.id, c.displayOrder)
              from ZoneCategory zc join  zc.category c
              where zc.zoneId in :zoneIds
              """)
  List<CategoryFlatRow> listAllForTreeWithinZones(Set<Long> zoneIds);
}
