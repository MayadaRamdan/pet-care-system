package com.petcare.common.common.embeddable;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class LocalizableString implements Localizable<String> {

  private String arabic;

  private String english;

  public static LocalizableString of(String english, String arabic) {
    LocalizableString localizable = new LocalizableString();
    localizable.arabic = arabic;
    localizable.english = english;
    return localizable;
  }
}
