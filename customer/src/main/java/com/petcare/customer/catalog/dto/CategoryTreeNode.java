package com.petcare.customer.catalog.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class CategoryTreeNode implements Serializable, Comparable<CategoryTreeNode> {

  private final Long id;
  private final String name;
  private final String path;
  private final String assetUrl;
  private final List<CategoryTreeNode> children = new ArrayList<>();
  private final Integer displayOrder;

  public CategoryTreeNode(
      Long id, String name, String path, String assetUrl, Integer displayOrder) {
    this.id = id;
    this.name = name;
    this.path = path;
    this.assetUrl = assetUrl;
    this.displayOrder = displayOrder;
  }

  public void addChild(CategoryTreeNode child) {
    this.children.add(child);
  }

  @Override
  public int compareTo(@NotNull CategoryTreeNode o) {

    if (displayOrder != null && o.displayOrder != null) {
      return displayOrder.compareTo(o.displayOrder);
    }

    if (displayOrder == null && o.displayOrder == null) {
      return id.compareTo(o.id);
    }

    return (displayOrder == null) ? -1 : 1;
  }
}
