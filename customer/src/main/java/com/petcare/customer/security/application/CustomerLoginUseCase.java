package com.petcare.customer.security.application;

import com.petcare.common.security.domain.DeviceTrackingInfo;
import com.petcare.common.security.dto.LoginRequest;
import com.petcare.customer.customer.domain.Customer;
import com.petcare.customer.customer.repository.CustomerRepository;
import com.petcare.customer.security.dto.AuthResponse;
import com.petcare.customer.security.dto.UserInfo;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerLoginUseCase {

  private final CustomerRepository customerRepository;
  private final GenerateAccessTokenUseCase generateAccessTokenUseCase;
  private final AuthenticationManager authenticationManager;

  @Transactional
  public AuthResponse login(LoginRequest request, DeviceTrackingInfo deviceTrackingInfo) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    Customer customer =
        customerRepository
            .findByEmail(request.username())
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found: " + request.username()));

    customer.setLastLogin(Instant.now());

    String accessToken = generateAccessTokenUseCase.execute(customer, deviceTrackingInfo);

    return new AuthResponse(
        accessToken,
        "Bearer", // Convert to seconds
        new UserInfo(
            customer.getId(), customer.getEmail(), customer.getName(), customer.getAvatarUrl()));
  }
}
