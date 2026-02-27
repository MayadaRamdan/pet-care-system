package com.petcare.admin.utils;

import jakarta.servlet.http.HttpServletRequest;

public class HttpServletRequestUtils {

  public static String getDeviceInfo(HttpServletRequest request) {
    String userAgent = request.getHeader("User-Agent");
    return userAgent != null ? userAgent : "Unknown";
  }

  public static String getClientIp(HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
      return xForwardedFor.split(",")[0].trim();
    }
    return request.getRemoteAddr();
  }
}
