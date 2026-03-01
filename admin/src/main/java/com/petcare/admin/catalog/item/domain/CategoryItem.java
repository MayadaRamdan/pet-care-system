package com.petcare.admin.catalog.item.domain;

import com.petcare.admin.catalog.category.domain.Category;
import com.petcare.common.catalog.domain.CategoryItemPK;
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
@IdClass(CategoryItemPK.class)
@Entity
@Table(name = "category_item")
public class CategoryItem {
  @Id private long categoryId;
  @Id private long itemId;

  @ManyToOne
  @MapsId("itemId")
  @JoinColumn(name = "item_id")
  private Item item;

  @ManyToOne
  @MapsId("categoryId")
  @JoinColumn(name = "category_id")
  private Category category;

  public static CategoryItem of(Category category, Item item) {
    CategoryItem ci = new CategoryItem();
    ci.setItem(item);
    ci.setItemId(item.getId());
    ci.setCategory(category);
    ci.setCategoryId(category.getId());
    return ci;
  }
}
