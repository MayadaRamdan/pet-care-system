package com.petcare.admin.catalog.category.application;

import com.petcare.admin.catalog.category.dto.CategoryListNode;
import com.petcare.admin.catalog.category.repository.CategoryRepository;
import com.petcare.common.common.dto.IdName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ListAllCategoriesUseCase {

  private final CategoryRepository categoryRepository;

  public List<IdName> execute() {
    Map<Long, List<CategoryListNode>> childrenMap = getChildrenMap();
    List<CategoryListNode> categories = new ArrayList<>();

    if (childrenMap.get(null) == null || childrenMap.isEmpty()) return List.of();

    childrenMap.get(null).stream()
        .sorted()
        .forEach(c -> treeToSortedList(c, childrenMap, categories));

    return categories.stream().map(c -> new IdName(c.getId(), c.getPath())).toList();
  }

  private Map<Long, List<CategoryListNode>> getChildrenMap() {
    List<CategoryListNode> allCategories = categoryRepository.listAllCategories();

    Map<Long, List<CategoryListNode>> childrenMap = new HashMap<>();

    for (CategoryListNode ct : allCategories) {

      if (!childrenMap.containsKey(ct.getParentId())) {
        childrenMap.put(ct.getParentId(), new ArrayList<>());
      }

      childrenMap.get(ct.getParentId()).add(ct);
    }

    return childrenMap;
  }

  private void treeToSortedList(
      CategoryListNode parent,
      Map<Long, List<CategoryListNode>> childrenMap,
      List<CategoryListNode> categories) {

    categories.add(parent);

    if (childrenMap.get(parent.getId()) == null) {
      return;
    }
    childrenMap.get(parent.getId()).stream()
        .sorted()
        .peek(c -> c.parentPath(parent.getPath()))
        .forEach(c -> treeToSortedList(c, childrenMap, categories));
  }
}
