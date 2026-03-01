package com.petcare.admin.security.application;

import com.petcare.admin.security.domain.SecurityToken;
import com.petcare.admin.security.repository.SecurityTokenRepository;
import com.petcare.admin.staffuser.domain.StaffUser;
import com.petcare.common.security.domain.DeviceTrackingInfo;
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

  @Value("${jwt.access-token-expiration}")
  private Long accessTokenExpiration;

  public void execute(
      String tokenId,
      String accessToken,
      StaffUser staffUser,
      DeviceTrackingInfo deviceTrackingInfo) {

    SecurityToken securityToken = new SecurityToken();
    securityToken.setId(tokenId);
    securityToken.setAccessToken(accessToken);
    securityToken.setStaffUser(staffUser);
    securityToken.setAccessTokenExpiresAt(
        Instant.now().plus(accessTokenExpiration, ChronoUnit.MINUTES));

    securityToken.setDeviceInfo(deviceTrackingInfo.device());
    securityToken.setIpAddress(deviceTrackingInfo.ip());

    tokenRepository.save(securityToken);
  }
}
