package com.petcare.common.exception.handler;

import com.petcare.common.exception.domain.BusinessException;
import com.petcare.common.exception.domain.ResourceNotFoundException;
import com.petcare.common.exception.domain.UnauthorizedException;
import com.petcare.common.exception.domain.ValidationException;
import com.petcare.common.exception.dto.ErrorResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

public abstract class BaseExceptionHandler {

  protected final MessageSource messageSource;

  protected BaseExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  protected String getMessage(String key, Object[] args) {
    return messageSource.getMessage(key, args, key, LocaleContextHolder.getLocale());
  }

  protected String getMessage(String key) {
    return messageSource.getMessage(key, null, key, LocaleContextHolder.getLocale());
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(
      ResourceNotFoundException ex, WebRequest request) {

    String message = getMessage(ex.getMessageKey(), ex.getArgs());

    ErrorResponse error =
        ErrorResponse.of(HttpStatus.NOT_FOUND, message, List.of(request.getDescription(false)));
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorResponse> handleValidation(
      ValidationException ex, WebRequest request) {

    String message = getMessage(ex.getMessageKey(), ex.getArgs());

    ErrorResponse error =
        ErrorResponse.of(HttpStatus.BAD_REQUEST, message, List.of(request.getDescription(false)));
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(
      BusinessException ex, WebRequest request) {

    String message = getMessage(ex.getMessageKey(), ex.getArgs());

    ErrorResponse error =
        ErrorResponse.of(HttpStatus.BAD_REQUEST, message, List.of(request.getDescription(false)));
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            error -> {
              String errorMessage = error.getDefaultMessage();
              errors.put(error.getField(), errorMessage);
            });

    return new ResponseEntity<>(ErrorResponse.validation(errors), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
    return new ResponseEntity<>(
        ErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage(), Collections.emptyList()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {

    String message = getMessage("petcare.error.internal.server", null);

    ErrorResponse error =
        ErrorResponse.of(
            HttpStatus.INTERNAL_SERVER_ERROR, message, List.of(request.getDescription(false)));

    // Log the actual exception for debugging
    logException(ex);

    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorized(
      UnauthorizedException ex, WebRequest request) {

    String message = getMessage(ex.getMessageKey());
    if (message == null) {
      message = "Unauthorized";
    }

    ErrorResponse error =
        ErrorResponse.of(HttpStatus.UNAUTHORIZED, message, List.of(request.getDescription(false)));
    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
  }

  // Hook method for services to override logging behavior
  public abstract void logException(Exception ex);
}
