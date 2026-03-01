package com.petcare.common.exception.domain;


public class ValidationException extends BaseException {
  public ValidationException(String field) {
    super("petcare.error.validation", field);
  }
}
