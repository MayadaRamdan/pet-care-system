package com.petcare.customer.security.application;

import com.petcare.common.security.domain.DeviceTrackingInfo;
import com.petcare.customer.customer.domain.Customer;
import com.petcare.customer.security.domain.SecurityToken;
import com.petcare.customer.security.repository.SecurityTokenRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class CreateSecurityTokenUseCase {

  @Autowired private SecurityTokenRepository tokenRepository;
  @Autowired private TokenCacheService tokenCacheService;

  @Value("${jwt.access-token-expiration}")
  private Long accessTokenExpiration;

  @Value("${jwt.refresh-token-expiration}")
  private Long refreshTokenExpiration;

  public void execute(
      Customer customer,
      String accessToken,
      String refreshToken,
      DeviceTrackingInfo deviceTrackingInfo) {

    SecurityToken securityToken = new SecurityToken();
    securityToken.setAccessToken(accessToken);
    securityToken.setRefreshToken(refreshToken);
    securityToken.setCustomer(customer);
    securityToken.setAccessTokenExpiresAt(
        Instant.now().plus(accessTokenExpiration, ChronoUnit.MINUTES));
    securityToken.setRefreshTokenExpiresAt(
        (Instant.now().plus(refreshTokenExpiration, ChronoUnit.DAYS)));

    securityToken.setDeviceInfo(deviceTrackingInfo.device());
    securityToken.setIpAddress(deviceTrackingInfo.ip());

    tokenRepository.save(securityToken);

    tokenCacheService.cacheToken(accessToken, customer.getId(), accessTokenExpiration * 1000 * 60);
  }
}
