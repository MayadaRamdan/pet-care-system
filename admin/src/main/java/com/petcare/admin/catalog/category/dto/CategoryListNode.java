package com.petcare.admin.catalog.category.dto;

import java.io.Serializable;
import lombok.Getter;

@Getter
public class CategoryListNode implements Serializable, Comparable<CategoryListNode> {

  private static final long serialVersionUID = -24082517L;

  private Long id;
  private String name;
  private String path;
  private Long parentId;

  public CategoryListNode(Long id, String name, String path, Long parentId) {
    this.id = id;
    this.name = name;
    this.path = path;
    this.parentId = parentId;
  }

  public void parentPath(String parentPath) {
    this.path = parentPath + " > " + name;
  }

  @Override
  public int compareTo(CategoryListNode o) {
    return name.compareTo(o.name);
  }
}
