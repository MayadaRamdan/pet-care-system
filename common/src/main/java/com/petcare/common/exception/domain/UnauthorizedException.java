package com.petcare.common.exception.domain;

import lombok.Getter;

public class UnauthorizedException extends RuntimeException {

  @Getter private String messageKey;

  public static UnauthorizedException unauthorized() {
    UnauthorizedException exception = new UnauthorizedException();
    exception.messageKey = "petneeds.error.unauthorized";
    return exception;
  }
}
