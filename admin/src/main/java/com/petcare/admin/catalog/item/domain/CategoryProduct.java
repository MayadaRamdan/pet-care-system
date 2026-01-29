package com.petcare.admin.catalog.item.domain;

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
@IdClass(CategoryProductPK.class)
@Entity
@Table(name = "category_product")
public class CategoryProduct {
  @Id private long categoryId;
  @Id private long itemId;

  @ManyToOne
  @MapsId("itemId")
  @JoinColumn(name = "item_id")
  private Item item;
}
