package com.petcare.common.elasticsearch.application;

import com.petcare.common.elasticsearch.domain.ZoneDocument;
import com.petcare.common.elasticsearch.domain.ZoneDocumentSearchResult;
import com.petcare.common.elasticsearch.dto.GeoPointDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ZoneDocumentSearchUseCase {

  private final ElasticsearchOperations elasticsearchOperations;

  public List<ZoneDocumentSearchResult> execute(GeoPointDto point) {
    String queryJson =
        String.format(
            """
            {
              "query": {
                "geo_shape": {
                  "area": {
                    "shape": {
                      "type": "point",
                      "coordinates": [%f, %f]
                    },
                    "relation": "contains"
                  }
                }
              }
            }
            """,
            point.lon(), point.lat());

    StringQuery searchQuery = new StringQuery(queryJson);

    List<SearchHit<ZoneDocument>> searchHits =
        elasticsearchOperations.search(searchQuery, ZoneDocument.class).getSearchHits();

    return searchHits.stream()
        .map(SearchHit::getContent)
        .map(zone -> new ZoneDocumentSearchResult(zone.getId(), zone.getName(), zone.getCode()))
        .collect(Collectors.toList());
  }
}
