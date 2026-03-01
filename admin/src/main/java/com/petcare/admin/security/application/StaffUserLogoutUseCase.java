package com.petcare.admin.security.application;

import com.petcare.admin.security.repository.SecurityTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class StaffUserLogoutUseCase {

  private final SecurityTokenRepository securityTokenRepository;

  public void execute(String accessToken) {
    securityTokenRepository.revoke(accessToken);
  }
}
