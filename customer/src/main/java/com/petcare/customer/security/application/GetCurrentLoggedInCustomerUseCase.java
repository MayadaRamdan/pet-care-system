package com.petcare.customer.security.application;

import com.petcare.customer.customer.domain.Customer;
import com.petcare.customer.customer.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class GetCurrentLoggedInCustomerUseCase {

  private final CustomerRepository customerRepository;

  public Customer execute() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated()) {
      return null;
    }

    return customerRepository.findByEmail(auth.getName()).orElse(null);
  }
}
