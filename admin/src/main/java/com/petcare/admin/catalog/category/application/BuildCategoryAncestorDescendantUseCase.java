package com.petcare.admin.catalog.category.application;

import com.petcare.admin.catalog.category.repository.CategoryAncestorDescendantRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class BuildCategoryAncestorDescendantUseCase {

  private final CategoryAncestorDescendantRepository ancestorDescendantRepository;
  private final JdbcTemplate jdbcTemplate;

  record AncestorDescendant(long ancestorId, long descendantId, int depth) {}

  public void execute() {
    log.info("Starting building categoryAncestorDescendant");

    // Build data FIRST, delete/insert LAST. This minimizes lock duration.
    List<AncestorDescendant> allRows = buildAllRows();

    // Locking starts here
    ancestorDescendantRepository.deleteAll();

    // Step 4: allRows insert (using batch)
    batchUpdate(allRows);
    log.info("Starting building categoryAncestorDescendant");
  }

  private List<AncestorDescendant> buildAllRows() {
    List<Object[]> rows = ancestorDescendantRepository.findCategoriesIdsAndParentsIds();
    Map<Long, Long> descendantIdToAncestorIdMap = new HashMap<>(rows.size());

    for (Object[] row : rows) {
      Long id = ((Number) row[0]).longValue();
      Long parentId = row[1] != null ? ((Number) row[1]).longValue() : null;
      descendantIdToAncestorIdMap.put(id, parentId);
    }

    // Step 3: build closure relations
    List<AncestorDescendant> allRows = new ArrayList<>(rows.size() * 3);

    for (Long descendantId : descendantIdToAncestorIdMap.keySet()) {
      Long ancestorId = descendantId;
      int depth = 0;
      while (ancestorId != null) {
        allRows.add(new AncestorDescendant(ancestorId, descendantId, depth++));
        ancestorId = descendantIdToAncestorIdMap.get(ancestorId);
      }
    }

    return allRows;
  }

  private void batchUpdate(List<AncestorDescendant> allRows) {
    jdbcTemplate.batchUpdate(
        "insert into category_ancestor_descendant (ancestor_id, descendant_id, depth) values( ?, ?, ?)",
        allRows,
        200,
        (ps, entry) -> {
          ps.setLong(1, entry.ancestorId);
          ps.setLong(2, entry.descendantId);
          ps.setInt(3, entry.depth);
        });
  }
}
