package com.petcare.admin.catalog.category.application;

import com.petcare.admin.catalog.category.domain.PathUpdateCategoryRow;
import com.petcare.admin.catalog.category.repository.CategoryRepository;
import com.petcare.common.common.domain.Constants;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
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
@AllArgsConstructor
@Transactional
public class UpdateCategoriesPathsUseCase {

  private final CategoryRepository categoryRepository;
  private final JdbcTemplate jdbcTemplate;

  private record PathValue(String en, String ar) {}

  private record CategoryGraph(
          Map<Long, PathUpdateCategoryRow> categoryMap,
          Map<Long, List<Long>> childrenMap,
          List<Long> roots) {}

  // -------------------------------------------------------------------------
  // Public entry point
  // -------------------------------------------------------------------------

  public void execute() {
    List<PathUpdateCategoryRow> rows = categoryRepository.findCategoriesRows();
    log.info("Starting path update for {} categories", rows.size());

    CategoryGraph graph = buildGraph(rows);
    Map<Long, PathValue> computedPaths = computeAllPaths(graph);
    batchUpdate(computedPaths);

    log.info("Successfully updated paths for {} categories", computedPaths.size());
  }

  // -------------------------------------------------------------------------
  // Step 1 — build lookup structures in a single pass
  // -------------------------------------------------------------------------

  private CategoryGraph buildGraph(List<PathUpdateCategoryRow> rows) {
    Map<Long, PathUpdateCategoryRow> categoryMap = new HashMap<>(rows.size() * 2);
    Map<Long, List<Long>> childrenMap = new HashMap<>(rows.size() * 2);
    List<Long> roots = new ArrayList<>();

    for (PathUpdateCategoryRow row : rows) {
      categoryMap.put(row.id(), row);

      if (row.parentId() == null) {
        roots.add(row.id());
      } else {
        childrenMap.computeIfAbsent(row.parentId(), k -> new ArrayList<>()).add(row.id());
      }
    }

    return new CategoryGraph(categoryMap, childrenMap, roots);
  }

  // -------------------------------------------------------------------------
  // Step 2 — compute paths iteratively (BFS) to avoid StackOverflowError
  // -------------------------------------------------------------------------

  private Map<Long, PathValue> computeAllPaths(CategoryGraph graph) {
    Map<Long, PathValue> computedPaths = new HashMap<>(graph.categoryMap().size() * 2);
    Deque<Long> queue = new ArrayDeque<>(graph.roots());

    while (!queue.isEmpty()) {
      Long id = queue.poll();
      PathUpdateCategoryRow row = graph.categoryMap().get(id);

      PathValue path = buildPath(row, computedPaths);
      computedPaths.put(id, path);

      List<Long> children = graph.childrenMap().get(id);
      if (children != null) {
        queue.addAll(children);
      }
    }

    return computedPaths;
  }

  private PathValue buildPath(PathUpdateCategoryRow row, Map<Long, PathValue> computedPaths) {
    if (row.parentId() == null) {
      return new PathValue(row.englishName(), row.arabicName());
    }

    PathValue parentPath = computedPaths.get(row.parentId());

    String en = String.join(Constants.CATEGORY_PATH_SEPARATOR, parentPath.en(), row.englishName());
    String ar = String.join(Constants.CATEGORY_PATH_SEPARATOR, parentPath.ar(), row.arabicName());

    return new PathValue(en, ar);
  }

  // -------------------------------------------------------------------------
  // Step 3 — persist in batches
  // -------------------------------------------------------------------------

  private void batchUpdate(Map<Long, PathValue> paths) {
    jdbcTemplate.batchUpdate(
            "UPDATE category SET english_path = ?, arabic_path = ? WHERE id = ?",
            paths.entrySet(),
            200,
            (ps, entry) -> {
              ps.setString(1, entry.getValue().en());
              ps.setString(2, entry.getValue().ar());
              ps.setLong(3, entry.getKey());
            });
  }
}