package com.petcare.common.elasticsearch.domain;

import lombok.AllArgsConstructor;import lombok.Getter;
import lombok.NoArgsConstructor;import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ZoneDocumentSearchResult {
  private Long id;
  private String name;
  private String code;
}
