package com.petcare.common.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoShapeField;
import org.springframework.data.elasticsearch.core.geo.GeoJsonPolygon;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(indexName = "zone")
public class ZoneDocument {

  @Id private Long id;

  @Field(type = FieldType.Text)
  private String name;

  @Field(type = FieldType.Text)
  private String code;

  @Field(type = FieldType.Text)
  private String status;

  @GeoShapeField private GeoJsonPolygon polygon;

  public static ZoneDocument of(Long id, String name, String code, GeoJsonPolygon polygon) {
    ZoneDocument document = new ZoneDocument();
    document.id = id;
    document.name = name;
    document.code = code;
    document.polygon = polygon;
    return document;
  }
}
