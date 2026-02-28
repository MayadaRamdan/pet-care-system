package com.petcare.admin.security.application;

import com.petcare.admin.security.domain.SecurityToken;
import com.petcare.admin.security.repository.SecurityTokenRepository;
import com.petcare.admin.staffuser.domain.StaffUser;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class StaffUserLogoutAllDevicesUseCase {

  private final SecurityTokenRepository securityTokenRepository;
  private final TokenCacheService tokenCacheService;
  private final GetCurrentLoggedInStaffUserUseCase getCurrentLoggedInStaffUserUseCase;

  public void execute() {
    StaffUser loggedInUser = getCurrentLoggedInStaffUserUseCase.execute();
    if (loggedInUser == null) {
      return;
    }

    Set<SecurityToken> tokens = securityTokenRepository.findAllByStaffUserId(loggedInUser.getId());
    for (SecurityToken t : tokens) {
      tokenCacheService.evictToken(t.getAccessToken());
    }

    securityTokenRepository.revokeAll(loggedInUser.getId());
  }
}
