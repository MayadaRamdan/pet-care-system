package com.petcare.customer.utils;

import com.petcare.common.security.domain.DeviceTrackingInfo;
import jakarta.servlet.http.HttpServletRequest;

public class HttpServletRequestUtils {

  private static String getDeviceInfo(HttpServletRequest request) {
    String userAgent = request.getHeader("User-Agent");
    return userAgent != null ? userAgent : "Unknown";
  }

  private static String getClientIp(HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
      return xForwardedFor.split(",")[0].trim();
    }
    return request.getRemoteAddr();
  }

  public static DeviceTrackingInfo getDeviceTrackingInfo(HttpServletRequest request) {
    return new DeviceTrackingInfo(getDeviceInfo(request), getClientIp(request));
  }
}
