package com.petcare.customer.security.controller;

import com.petcare.common.common.response.ApiResponse;
import com.petcare.common.security.dto.LoginRequest;
import com.petcare.customer.security.application.actions.CustomerLoginUseCase;
import com.petcare.customer.security.application.actions.CustomerLogoutAllDevicesUseCase;
import com.petcare.customer.security.application.actions.CustomerLogoutUseCase;
import com.petcare.customer.security.application.actions.RegisterCustomerUseCase;
import com.petcare.customer.security.dto.AuthResponse;
import com.petcare.customer.security.dto.CustomerRegisterRequest;
import com.petcare.customer.utils.HttpServletRequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class CustomerAuthController {

  private final RegisterCustomerUseCase registerCustomerUseCase;
  private final CustomerLoginUseCase customerLoginUseCase;
  private final CustomerLogoutUseCase customerLogoutUseCase;
  private final CustomerLogoutAllDevicesUseCase customerLogoutAllDevicesUseCase;

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(
      @Valid @RequestBody CustomerRegisterRequest request, HttpServletRequest httpRequest) {
    log.info("🎯 Register endpoint hit!");
    AuthResponse response =
        registerCustomerUseCase.execute(
            request, HttpServletRequestUtils.getDeviceTrackingInfo(httpRequest));
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(
      @Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
    log.info("🔐 Login endpoint hit!");
    AuthResponse response =
        customerLoginUseCase.login(
            request, HttpServletRequestUtils.getDeviceTrackingInfo(httpRequest));
    return ResponseEntity.ok(response);
  }

  @PostMapping("/logout")
  public ResponseEntity<ApiResponse> logout(HttpServletRequest httpRequest) {
    log.info("Logout endpoint hit!");
    customerLogoutUseCase.execute(HttpServletRequestUtils.getJwtFromRequest(httpRequest));
    return ResponseEntity.ok(ApiResponse.success());
  }

  @PostMapping("/logout-all-devices")
  public ResponseEntity<ApiResponse> logoutFromAllDevices() {
    log.info("logoutFromAllDevices endpoint hit!");
    customerLogoutAllDevicesUseCase.execute();
    return ResponseEntity.ok(ApiResponse.success());
  }
}
