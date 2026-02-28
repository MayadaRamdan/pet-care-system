package com.petcare.customer.security.application;

import com.petcare.customer.customer.domain.Customer;
import com.petcare.customer.security.domain.SecurityToken;
import com.petcare.customer.security.dto.AuthResponse;
import com.petcare.customer.security.dto.UserInfo;
import com.petcare.customer.security.repository.SecurityTokenRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class RefreshTokenUseCase {

  @Autowired private SecurityTokenRepository tokenRepository;
  @Autowired private TokenCacheService tokenCacheService;

  @Value("${jwt.access-token-expiration}")
  private Long accessTokenExpiration;

  @Value("${jwt.refresh-token-expiration}")
  private Long refreshTokenExpiration;

  public AuthResponse execute(String refreshToken) {
    SecurityToken securityToken =
        tokenRepository
            .findByAccessToken(refreshToken)
            .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

    Customer customer = securityToken.getCustomer();

    String newRefreshToken = UUID.randomUUID().toString();

    securityToken.setRefreshToken(newRefreshToken);
    securityToken.setAccessTokenExpiresAt(
        Instant.now().plus(accessTokenExpiration, ChronoUnit.MINUTES));
    securityToken.setRefreshTokenExpiresAt(
        (Instant.now().plus(refreshTokenExpiration, ChronoUnit.DAYS)));

    tokenCacheService.cacheToken(
        securityToken.getAccessToken(), customer.getId(), accessTokenExpiration * 1000 * 60);

    UserInfo userInfo =
        new UserInfo(
            customer.getId(), customer.getEmail(), customer.getName(), customer.getAvatarUrl());

    return new AuthResponse(securityToken.getAccessToken(), refreshToken, "Bearer", userInfo);
  }
}
