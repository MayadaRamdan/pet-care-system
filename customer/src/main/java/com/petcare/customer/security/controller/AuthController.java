package com.petcare.customer.security.controller;

import com.petcare.common.security.dto.LoginRequest;
import com.petcare.customer.security.dto.CustomerRegisterRequest;
import com.petcare.customer.security.application.AuthService;
import com.petcare.customer.security.dto.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody CustomerRegisterRequest request) {
    System.out.println("🎯 Register endpoint hit!");
    AuthResponse response = authService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(
      @Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {

    System.out.println("🔐 Login endpoint hit!");
    AuthResponse response = authService.login(request, httpRequest);
    return ResponseEntity.ok(response);
  }
}
