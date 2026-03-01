package com.petcare.common.exception.domain;

public class ResourceNotFoundException extends BaseException {
  public ResourceNotFoundException(Object resourceId) {
    super("petcare.error.resource.notfound", resourceId);
  }

  public ResourceNotFoundException(String resourceType, Object resourceId) {
    super("petcare.error.resource.notfound.typed", resourceType, resourceId);
  }

  public static ResourceNotFoundException from(String resourceType) {
    return new ResourceNotFoundException(resourceType);
  }
}
