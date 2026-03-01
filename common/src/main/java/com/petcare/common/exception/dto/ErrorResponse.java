package com.petcare.common.exception.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse implements Serializable {
  private String id;
  private HttpStatus status;
  private String message;
  private String code;
  private LocalDateTime timestamp;
  private Map<String, String> validationErrors;
  private List<String> errors;

  public static ErrorResponse of(HttpStatus status, String message) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.id = getTimeStamp();
    errorResponse.timestamp = LocalDateTime.now();

    errorResponse.status = status;
    errorResponse.message = message;
    return errorResponse;
  }

  public static ErrorResponse of(HttpStatus status, String message, List<String> errors) {
    ErrorResponse errorResponse = of(status, message);
    errorResponse.errors = errors;
    return errorResponse;
  }

  public static ErrorResponse validation(Map<String, String> errors) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.id = getTimeStamp();
    errorResponse.timestamp = LocalDateTime.now();
    errorResponse.status = HttpStatus.BAD_REQUEST;
    errorResponse.message = "Validation error";
    errorResponse.code = "VALIDATION_ERROR";

    errorResponse.validationErrors = errors;

    return errorResponse;
  }

  private static String getTimeStamp() {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
  }
}
