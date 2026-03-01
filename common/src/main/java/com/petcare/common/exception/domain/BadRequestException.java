package com.petcare.common.exception.domain;

import java.util.Collections;

public class BadRequestException extends BaseException {
  public BadRequestException(String messageKey, Object... args) {
    super(messageKey, args);
  }

  public BadRequestException(String messageKey) {
    super(messageKey, Collections.emptyList());
  }
}
