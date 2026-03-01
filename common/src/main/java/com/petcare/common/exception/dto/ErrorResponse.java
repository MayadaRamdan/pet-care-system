package com.petcare.common.exception.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ErrorResponse implements Serializable {
  private HttpStatus status;
  private String message;
  private String code;
  private LocalDateTime timestamp;
  private Map<String, String> validationErrors;
  private List<String> errors;

  public static ErrorResponse of(HttpStatus status, String message, List<String> errors) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.timestamp = LocalDateTime.now();

    errorResponse.status = status;
    errorResponse.message = message;
    errorResponse.errors = errors;

    return errorResponse;
  }

  public static ErrorResponse validation(Map<String, String> errors) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.timestamp = LocalDateTime.now();
    errorResponse.status = HttpStatus.BAD_REQUEST;
    errorResponse.message = "Validation error";
    errorResponse.code = "VALIDATION_ERROR";

    errorResponse.validationErrors = errors;

    return errorResponse;
  }
}
