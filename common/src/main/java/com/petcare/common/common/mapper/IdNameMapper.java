package com.petcare.common.common.mapper;

import com.petcare.common.common.dto.IdName;
import com.petcare.common.common.embeddable.LocalizableString;

public class IdNameMapper {

  public static IdName toIdName(Long id, String name) {
    return new IdName(id, name);
  }

  public static IdName toIdName(Long id, LocalizableString name) {
    return new IdName(id, name.getEnglish());
  }
}
