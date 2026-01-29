package com.petcare.common.exception.handler;

import com.petcare.common.exception.domain.BusinessException;
import com.petcare.common.exception.domain.ResourceNotFoundException;
import com.petcare.common.exception.domain.ValidationException;
import com.petcare.common.exception.dto.ErrorResponse;
import java.util.HashMap;
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
    return messageSource.getMessage(
        key,
        args,
        key, // default message if key not found
        LocaleContextHolder.getLocale());
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(
      ResourceNotFoundException ex, WebRequest request) {

    String message = getMessage(ex.getMessageKey(), ex.getArgs());

    ErrorResponse error =
        new ErrorResponse(HttpStatus.NOT_FOUND.value(), message, request.getDescription(false));
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorResponse> handleValidation(
      ValidationException ex, WebRequest request) {

    String message = getMessage(ex.getMessageKey(), ex.getArgs());

    ErrorResponse error =
        new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message, request.getDescription(false));
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  //  @ExceptionHandler(UnauthorizedException.class)
  //  public ResponseEntity<ErrorResponse> handleUnauthorized(
  //          UnauthorizedException ex, WebRequest request) {
  //
  //    String message = getMessage(ex.getMessageKey(), ex.getArgs());
  //
  //    ErrorResponse error = new ErrorResponse(
  //            HttpStatus.UNAUTHORIZED.value(),
  //            message,
  //            request.getDescription(false)
  //    );
  //    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
  //  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(
      BusinessException ex, WebRequest request) {

    String message = getMessage(ex.getMessageKey(), ex.getArgs());

    ErrorResponse error =
        new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message, request.getDescription(false));
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationErrors(
      MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            error -> {
              String errorMessage = error.getDefaultMessage();
              errors.put(error.getField(), errorMessage);
            });

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {

    String message = getMessage("petcare.error.internal.server", null);

    ErrorResponse error =
        new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(), message, request.getDescription(false));

    // Log the actual exception for debugging
    logException(ex);

    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // Hook method for services to override logging behavior
  public abstract void logException(Exception ex);
}
