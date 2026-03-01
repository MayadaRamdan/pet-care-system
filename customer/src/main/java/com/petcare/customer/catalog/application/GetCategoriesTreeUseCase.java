package com.petcare.customer.catalog.application;

import com.petcare.customer.catalog.dto.CategoryTreeNode;
import com.petcare.customer.catalog.repository.CategoryRepository;
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
public class GetCategoriesTreeUseCase {

  private final CategoryRepository categoryRepository;

  public List<CategoryTreeNode> execute() {
    // var rows = categoryRepository.listAllForTreeWithinZones(Set.of(1L));
    var rows = categoryRepository.listAllForTree();

    Map<Long, CategoryTreeNode> byId = new HashMap<>(rows.size());
    for (var row : rows) {
      byId.put(
          row.id(),
          new CategoryTreeNode(
              row.id(),
              row.name().getEnglish(),
              row.path().getEnglish(),
              row.thumbnailUrl(),
              row.displayOrder()));
    }

    List<CategoryTreeNode> roots = new ArrayList<>();
    for (var row : rows) {
      var node = byId.get(row.id());
      if (row.parentId() == null) {
        roots.add(node);
      } else {
        var parent = byId.get(row.parentId());
        if (parent != null) {
          parent.addChild(node);
        } else {
          // Parent missing (data inconsistency) => treat as root to avoid losing nodes
          roots.add(node);
        }
      }
    }

    for (var r : roots) {
      sortChildren(r);
    }

    return roots;
  }

  private void sortChildren(CategoryTreeNode node) {
    if (node.getChildren() == null || node.getChildren().isEmpty()) {
      return;
    }
    node.getChildren().sort(CategoryTreeNode::compareTo);
    for (var child : node.getChildren()) {
      sortChildren(child);
    }
  }
}
