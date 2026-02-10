package com.petcare.customer.security.application;

import com.petcare.customer.customer.domain.Customer;
import com.petcare.customer.customer.repository.CustomerRepository;
import com.petcare.customer.security.domain.CustomerPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final CustomerRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Customer user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    return new CustomerPrincipal(user);
  }

  @Transactional(readOnly = true)
  public UserDetails loadUserById(Long id) {
    Customer user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

    return new CustomerPrincipal(user);
  }
}
