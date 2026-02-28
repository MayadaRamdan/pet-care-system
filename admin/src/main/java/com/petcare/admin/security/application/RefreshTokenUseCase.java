package com.petcare.admin.security.application;

import com.petcare.admin.security.domain.SecurityToken;
import com.petcare.admin.security.domain.StaffUserRole;
import com.petcare.admin.security.dto.AuthResponse;
import com.petcare.admin.security.dto.StaffUserInfo;
import com.petcare.admin.security.repository.SecurityTokenRepository;
import com.petcare.admin.staffuser.domain.StaffUser;
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

    StaffUser staffUser = securityToken.getStaffUser();

    String newRefreshToken = UUID.randomUUID().toString();

    securityToken.setRefreshToken(newRefreshToken);
    securityToken.setAccessTokenExpiresAt(
        Instant.now().plus(accessTokenExpiration, ChronoUnit.MINUTES));
    securityToken.setRefreshTokenExpiresAt(
        (Instant.now().plus(refreshTokenExpiration, ChronoUnit.DAYS)));

    tokenCacheService.cacheToken(
        securityToken.getAccessToken(), staffUser.getId(), accessTokenExpiration * 1000 * 60);

    StaffUserRole role = staffUser.getRole();

    StaffUserInfo staffUserInfo =
        new StaffUserInfo(
            staffUser.getId(),
            staffUser.getUsername(),
            staffUser.getName(),
            staffUser.getEmail(),
            role.getName(),
            role.getPermissions());

    return new AuthResponse(securityToken.getAccessToken(), refreshToken, "Bearer", staffUserInfo);
  }
}
