package com.petcare.admin.catalog.category.repository;

import com.petcare.admin.catalog.category.domain.CategoryAncestorDescendant;
import com.petcare.common.catalog.domain.CategoryAncestorDescendantPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryAncestorDescendantRepository
    extends JpaRepository<CategoryAncestorDescendant, CategoryAncestorDescendantPK> {

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(value = "delete from CategoryAncestorDescendant")
  void deleteAll();

  @Query("select c.id, c.parent.id from Category c")
  List<Object[]> findCategoriesIdsAndParentsIds();
}
