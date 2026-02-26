package com.petcare.admin.catalog.category.application;

import com.petcare.admin.catalog.category.domain.PathUpdateCategoryRow;
import com.petcare.admin.catalog.category.repository.CategoryRepository;
import com.petcare.common.common.domain.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

  record PathValue(String en, String ar) {}

  public void execute() {

    List<PathUpdateCategoryRow> rows = categoryRepository.findCategoriesRows();

    Map<Long, PathUpdateCategoryRow> categoryMap =
        rows.stream().collect(Collectors.toMap(PathUpdateCategoryRow::id, r -> r));

    Map<Long, List<Long>> childrenMap = buildChildrenMap(rows);

    Map<Long, PathValue> computedPaths = new HashMap<>();

    for (PathUpdateCategoryRow row : rows) {
      if (row.parentId() == null) {
        computePathRecursive(row.id(), categoryMap, childrenMap, computedPaths);
      }
    }

    batchUpdate(computedPaths);
  }

  private Map<Long, List<Long>> buildChildrenMap(List<PathUpdateCategoryRow> rows) {
    Map<Long, List<Long>> childrenMap = new HashMap<>();

    for (PathUpdateCategoryRow r : rows) {
      childrenMap.computeIfAbsent(r.parentId(), k -> new ArrayList<>()).add(r.id());
    }

    return childrenMap;
  }

  private void computePathRecursive(
      Long id,
      Map<Long, PathUpdateCategoryRow> categoryMap,
      Map<Long, List<Long>> childrenMap,
      Map<Long, PathValue> computedPaths) {

    PathUpdateCategoryRow row = categoryMap.get(id);

    PathValue parentPath = null;

    if (row.parentId() != null) {
      parentPath = computedPaths.get(row.parentId());
    }

    String en =
        parentPath == null
            ? row.englishName()
            : parentPath.en() + Constants.CATEGORY_PATH_SEPARATOR + row.englishName();

    String ar =
        parentPath == null
            ? row.arabicName()
            : parentPath.ar() + Constants.CATEGORY_PATH_SEPARATOR + row.arabicName();

    PathValue path = new PathValue(en, ar);

    computedPaths.put(id, path);

    List<Long> children = childrenMap.get(id);

    if (children != null) {
      for (Long child : children) {
        computePathRecursive(child, categoryMap, childrenMap, computedPaths);
      }
    }
  }

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
