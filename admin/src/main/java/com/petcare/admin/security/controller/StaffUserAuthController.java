package com.petcare.admin.security.controller;

import com.petcare.admin.security.application.StaffUserLoginUseCase;
import com.petcare.admin.security.application.StaffUserLogoutUseCase;
import com.petcare.admin.security.dto.AuthResponse;
import com.petcare.common.common.response.ApiResponse;
import com.petcare.common.security.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class StaffUserAuthController {

  private final StaffUserLoginUseCase staffUserLoginUseCase;
  private final StaffUserLogoutUseCase staffUserLogoutUseCase;

  @PostMapping("/login")
  public ResponseEntity<ApiResponse> login(
      @Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
    log.info("🔐 Login endpoint hit!");
    AuthResponse response = staffUserLoginUseCase.execute(request, httpRequest);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping("/logout")
  public ResponseEntity<ApiResponse> logout(
      @RequestParam(name = "refresh-token") String refreshToken) {
    log.info("Logout endpoint hit!");
    staffUserLogoutUseCase.execute(refreshToken);
    return ResponseEntity.ok(ApiResponse.success());
  }
}
