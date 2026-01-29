package com.petcare.common.exception.domain;

public class BusinessException extends BaseException {
  public BusinessException(String messageKey, Object... args) {
    super(messageKey, args);
  }
}
