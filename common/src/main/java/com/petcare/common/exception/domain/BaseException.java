package com.petcare.common.exception.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// Base exception with message key support
public abstract class BaseException extends RuntimeException {
  private final String messageKey;
  private final Object[] args;

  protected BaseException(String messageKey, Object... args) {
    super(messageKey);
    this.messageKey = messageKey;
    this.args = args;
  }
}
