package com.petcare.customer.security.controller;

import com.petcare.customer.security.domain.CustomerPrincipal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class UserController {

  @GetMapping("/me")
  public ResponseEntity<Map<String, Object>> getCurrentUser(
      @AuthenticationPrincipal CustomerPrincipal userPrincipal) {
    return ResponseEntity.ok(
        Map.of(
            "id", userPrincipal.getId(),
            "username", userPrincipal.getUsername(),
            "email", userPrincipal.getCustomer().getEmail(),
            "fullName",
                userPrincipal.getCustomer().getFullName() != null
                    ? userPrincipal.getCustomer().getFullName()
                    : "",
            "avatarUrl",
                userPrincipal.getCustomer().getAvatarUrl() != null
                    ? userPrincipal.getCustomer().getAvatarUrl()
                    : "",
            "emailVerified", userPrincipal.getCustomer().isEmailVerified()));
  }

  @GetMapping("/profile")
  public ResponseEntity<Map<String, Object>> getProfile(
      @AuthenticationPrincipal CustomerPrincipal userPrincipal) {
    var user = userPrincipal.getCustomer();
    return ResponseEntity.ok(
        Map.of(
            "id", user.getId(),
            "email", user.getEmail(),
            "fullName", user.getFullName() != null ? user.getFullName() : "",
            "avatarUrl", user.getAvatarUrl() != null ? user.getAvatarUrl() : "",
            //     "phoneNumber", user.getPhoneNumber() != null ? user.getPhoneNumber() : "",
            "emailVerified", user.isEmailVerified(),
            "createdAt", user.getCreatedAt(),
            "lastLogin", user.getLastLogin()));
  }
}
