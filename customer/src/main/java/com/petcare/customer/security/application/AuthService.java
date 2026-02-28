package com.petcare.customer.security.application;

import com.petcare.common.exception.domain.BadRequestException;
import com.petcare.common.exception.domain.ResourceNotFoundException;
import com.petcare.common.security.dto.LoginRequest;
import com.petcare.customer.security.dto.RefreshTokenRequest;
import com.petcare.customer.security.dto.CustomerRegisterRequest;
import com.petcare.customer.customer.domain.Customer;
import com.petcare.customer.customer.repository.CustomerRepository;
import com.petcare.customer.redis.application.RefreshTokenService;
import com.petcare.customer.redis.domain.RefreshToken;
import com.petcare.customer.security.dto.AuthResponse;
import com.petcare.customer.security.dto.UserInfo;
import com.petcare.customer.utils.HttpServletRequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final CustomerRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final RefreshTokenService refreshTokenService;
  private final AuthenticationManager authenticationManager;

  @Value("${jwt.access-token-expiration}")
  private Long accessTokenExpiration;

  @Transactional
  public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {
    // Authenticate user
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Get user
    Customer user =
        userRepository
            .findByEmail(request.username())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    // Update last login
    user.setLastLogin(Instant.now());
    userRepository.save(user);

    // Generate tokens
    String accessToken = jwtService.generateAccessToken(user);
    RefreshToken refreshToken =
        refreshTokenService.createRefreshToken(
            user.getId(),
            HttpServletRequestUtils.getDeviceInfo(httpRequest),
            HttpServletRequestUtils.getClientIp(httpRequest));

    log.info("User logged in successfully: {}", user.getEmail());

    return buildAuthResponse(accessToken, refreshToken.getToken(), user);
  }

  @Transactional
  public AuthResponse register(CustomerRegisterRequest request) {
    // Check if username exists
    if (userRepository.existsByEmail(request.email())) {
      throw new BadRequestException("Username is already taken");
    }

    // Check if email exists
    if (userRepository.existsByEmail(request.email())) {
      throw new BadRequestException("Email is already registered");
    }

    // Create user (NO ROLES!)
    Customer user = new Customer();
    user.setEmail(request.email());
    user.setPassword(passwordEncoder.encode(request.password()));
    user.setName(request.name());
    user.setActive(true);
    user.setDeleted(false);
    user.setEmailVerified(false);

    user = userRepository.save(user);

    log.info("New customer registered: {}", user.getEmail());

    // Auto-login after registration
    String accessToken = jwtService.generateAccessToken(user);
    RefreshToken refreshToken =
        refreshTokenService.createRefreshToken(user.getId(), "registration", "system");

    return buildAuthResponse(accessToken, refreshToken.getToken(), user);
  }

  @Transactional
  public AuthResponse refreshToken(RefreshTokenRequest request, HttpServletRequest httpRequest) {
    // Validate refresh token
    RefreshToken refreshToken =
        refreshTokenService
            .findByToken(request.refreshToken())
            .orElseThrow(() -> new BadRequestException("Invalid refresh token"));

    // Get user
    Customer user =
        userRepository
            .findById(refreshToken.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    // Delete old refresh token
    refreshTokenService.deleteByToken(request.refreshToken());

    // Generate new tokens
    String accessToken = jwtService.generateAccessToken(user);
    RefreshToken newRefreshToken =
        refreshTokenService.createRefreshToken(
            user.getId(),
            HttpServletRequestUtils.getDeviceInfo(httpRequest),
            HttpServletRequestUtils.getClientIp(httpRequest));

    log.info("Token refreshed for user: {}", user.getEmail());

    return buildAuthResponse(accessToken, newRefreshToken.getToken(), user);
  }

  @Transactional
  public void logout(String refreshTokenValue) {
    if (refreshTokenValue != null && !refreshTokenValue.isEmpty()) {
      refreshTokenService.deleteByToken(refreshTokenValue);
      log.info("User logged out successfully");
    }
  }

  @Transactional
  public void logoutAll(Long userId) {
    refreshTokenService.deleteAllByUserId(userId);
    log.info("All sessions logged out for user ID: {}", userId);
  }

  private AuthResponse buildAuthResponse(String accessToken, String refreshToken, Customer user) {
    return new AuthResponse(
        accessToken,
        refreshToken,
        "Bearer",
        accessTokenExpiration / 1000, // Convert to seconds
        new UserInfo(user.getId(), user.getEmail(), user.getName(), user.getAvatarUrl()));
  }
}
