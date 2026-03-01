package com.petcare.admin.security.controller;

import com.petcare.common.common.response.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/public/passwords")
@AllArgsConstructor
public class PasswordHashingUtilController {

  private final PasswordEncoder passwordEncoder;

  @PostMapping("/hash")
  public ResponseEntity<ApiResponse> hashPassword(
      @RequestParam(name = "password") String password) {
    log.info("hashPassword endpoint hit!");
    return ResponseEntity.ok(ApiResponse.success(passwordEncoder.encode(password)));
  }
}
