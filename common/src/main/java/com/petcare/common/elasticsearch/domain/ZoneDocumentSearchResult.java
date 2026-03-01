package com.petcare.common.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZoneDocumentSearchResult {
  private Long id;
  private String name;
  private String code;

  public static ZoneDocumentSearchResult of(Long id, String name, String code) {
    ZoneDocumentSearchResult result = new ZoneDocumentSearchResult();
    result.id = id;
    result.name = name;
    result.code = code;
    return result;
  }
}
