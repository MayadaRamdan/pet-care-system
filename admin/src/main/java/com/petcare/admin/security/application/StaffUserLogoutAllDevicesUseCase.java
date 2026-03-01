package com.petcare.admin.security.application;

import com.petcare.admin.security.repository.SecurityTokenRepository;
import com.petcare.admin.staffuser.domain.StaffUser;
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
  private final GetCurrentLoggedInStaffUserUseCase getCurrentLoggedInStaffUserUseCase;

  public void execute() {
    StaffUser loggedInUser = getCurrentLoggedInStaffUserUseCase.execute();
    if (loggedInUser == null) {
      return;
    }

    securityTokenRepository.revokeAll(loggedInUser.getId());
  }
}
