package com.petcare.common.common.embeddable;

import static com.petcare.common.common.utils.StringUtils.EMPTY_STRING;
import static com.petcare.common.common.utils.StringUtils.ONE_SPACE_STRING;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class Name {

  private String first;
  private String last;

  public String getFullName() {
    if (first != null && last != null) {
      return first + ONE_SPACE_STRING + last;
    }
    if (first != null) return first;

    return (last == null) ? EMPTY_STRING : last;
  }
}
