package com.petcare.customer.security.application.actions;

import com.petcare.customer.security.repository.SecurityTokenRepository;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class CustomerLogoutUseCase {

  private final SecurityTokenRepository securityTokenRepository;

  public void execute(String accessToken) {
    securityTokenRepository.revoke(accessToken, Instant.now());
  }
}
