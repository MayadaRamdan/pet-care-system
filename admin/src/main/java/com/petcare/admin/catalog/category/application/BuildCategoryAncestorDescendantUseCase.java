package com.petcare.admin.catalog.category.application;

import com.petcare.admin.catalog.category.domain.CategoryAncestorDescendant;
import com.petcare.admin.catalog.category.repository.CategoryAncestorDescendantRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class BuildCategoryAncestorDescendantUseCase {

  private final CategoryAncestorDescendantRepository categoryAncestorDescendantRepository;

  public void execute() {

    // Build data FIRST, delete/insert LAST. This minimizes lock duration.
    List<CategoryAncestorDescendant> allRows = buildAllRows();

    // Locking starts here
    categoryAncestorDescendantRepository.deleteAll();

    // Step 4: allRows insert
    // TODO: Using batching is better
    categoryAncestorDescendantRepository.saveAll(allRows);
  }

  private List<CategoryAncestorDescendant> buildAllRows() {
    List<Object[]> rows = categoryAncestorDescendantRepository.findCategoriesIdsAndParentsIds();
    Map<Long, Long> descendantIdToAncestorIdMap = new HashMap<>(rows.size());

    for (Object[] row : rows) {
      Long id = ((Number) row[0]).longValue();
      Long parentId = row[1] != null ? ((Number) row[1]).longValue() : null;
      descendantIdToAncestorIdMap.put(id, parentId);
    }

    // Step 3: build closure relations
    List<CategoryAncestorDescendant> allRows = new ArrayList<>(rows.size() * 3);

    for (Long descendantId : descendantIdToAncestorIdMap.keySet()) {
      Long ancestorId = descendantId;
      int depth = 0;
      while (ancestorId != null) {
        allRows.add(CategoryAncestorDescendant.of(ancestorId, descendantId, depth++));
        ancestorId = descendantIdToAncestorIdMap.get(ancestorId);
      }
    }

    return allRows;
  }
}
