package com.petcare.admin.catalog.category.domain;

import com.petcare.common.catalog.domain.CategoryAncestorDescendantPK;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@IdClass(CategoryAncestorDescendantPK.class)
@Entity
@Table(name = "category_ancestor_descendant")
public class CategoryAncestorDescendant {

  @Id private long ancestorId;
  @Id private long descendantId;

  private int depth;

  public static CategoryAncestorDescendant of(Long categoryId, Long descendantId, int depth) {
    CategoryAncestorDescendant cd = new CategoryAncestorDescendant();
    cd.ancestorId = categoryId;
    cd.descendantId = descendantId;
    cd.depth = depth;
    return cd;
  }
}
