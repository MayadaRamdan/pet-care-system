package com.petcare.admin.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler
    extends com.petcare.common.exception.handler.BaseExceptionHandler {
  private GlobalExceptionHandler(final MessageSource messageSource) {
    super(messageSource);
  }

  @Override
  public void logException(Exception ex) {
    log.error("Exception occurred: ", ex);
  }
}
