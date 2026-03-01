package com.petcare.customer.security.application.actions;

import com.petcare.customer.customer.domain.Customer;
import com.petcare.customer.security.application.GetCurrentLoggedInCustomerUseCase;
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
public class CustomerLogoutAllDevicesUseCase {

  private final SecurityTokenRepository securityTokenRepository;
  private final GetCurrentLoggedInCustomerUseCase getCurrentLoggedInCustomerUseCase;

  public void execute() {
    Customer loggedInUser = getCurrentLoggedInCustomerUseCase.execute();
    if (loggedInUser == null) {
      return;
    }
    securityTokenRepository.revokeAll(loggedInUser.getId(), Instant.now());
  }
}
