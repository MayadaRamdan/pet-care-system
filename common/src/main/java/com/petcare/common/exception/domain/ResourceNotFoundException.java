package com.petcare.common.exception.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends BaseException {
  public ResourceNotFoundException(Object resourceId) {
    super("petcare.error.resource.notfound", resourceId);
  }

  public ResourceNotFoundException(String resourceType, Object resourceId) {
    super("petcare.error.resource.notfound.typed", resourceType, resourceId);
  }
}
