package com.petcare.common.common.response;

import com.petcare.common.common.utils.StringUtils;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse implements Serializable {

  private boolean success;
  private Object data;
  private String errorMessage;

  public static ApiResponse success(Object data) {
    ApiResponse response = new ApiResponse();
    response.success = true;
    response.errorMessage = StringUtils.EMPTY_STRING;
    response.data = data;
    return response;
  }
}
