package com.petcare.common.exception.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ErrorResponse implements Serializable {
  private final int status;
  private final String message;
  private final String path;
  private final LocalDateTime timestamp;

  public ErrorResponse(int status, String message, String path) {
    this.status = status;
    this.message = message;
    this.path = path;
    this.timestamp = LocalDateTime.now();
  }
}
