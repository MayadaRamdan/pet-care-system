package com.petcare.common.elasticsearch.application;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.GeoShapeRelation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.petcare.common.elasticsearch.domain.ZoneDocumentSearchResult;
import com.petcare.common.elasticsearch.dto.GeoPointDto;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ZoneDocumentSearchUseCase {

  private final ElasticsearchClient elasticsearchClient;

  public List<ZoneDocumentSearchResult> execute(GeoPointDto point) throws IOException {

    String geoJsonString =
        String.format("{\"type\":\"Point\",\"coordinates\":[%f,%f]}", point.lon(), point.lat());

    Query geoShapeFilter =
        Query.of(
            q ->
                q.geoShape(
                    gs ->
                        gs.field("polygon")
                            .shape(
                                s ->
                                    s.shape(JsonData.from(new StringReader(geoJsonString)))
                                        .relation(GeoShapeRelation.Intersects))));

    Query statusFilter = Query.of(q -> q.term(t -> t.field("status").value("active")));
    Query query = Query.of(q -> q.bool(b -> b.filter(geoShapeFilter).filter(statusFilter)));

    SearchRequest searchRequest = SearchRequest.of(s -> s.index("zone").query(query).size(100));

    SearchResponse<ZoneDocumentSearchResult> response =
        elasticsearchClient.search(searchRequest, ZoneDocumentSearchResult.class);
    return response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
  }
}
