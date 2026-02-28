package com.petcare.customer.security.application;


import com.petcare.common.security.domain.DeviceTrackingInfo;
import com.petcare.common.security.dto.LoginRequest;
import com.petcare.customer.customer.domain.Customer;
import com.petcare.customer.security.domain.CustomerPrincipal;
import com.petcare.customer.security.dto.AuthResponse;
import com.petcare.customer.security.dto.UserInfo;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerLoginUseCase {

  private final CustomerDetailsService customerDetailsService;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final CreateSecurityTokenUseCase createSecurityTokenUseCase;

  @Transactional
  public AuthResponse login(LoginRequest request, DeviceTrackingInfo deviceTrackingInfo) {
    // Authenticate user
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Get user
    Customer  customer =( (CustomerPrincipal)
            customerDetailsService.loadUserByUsername(request.username())).getCustomer();

    // Update last login
    customer.setLastLogin(Instant.now());

    // Generate tokens
    // Auto-login after registration
    String accessToken = jwtService.generateAccessToken(customer);

    String refreshToken = UUID.randomUUID().toString();

    createSecurityTokenUseCase.execute(customer, accessToken, refreshToken, deviceTrackingInfo);

    return new AuthResponse(
        accessToken,
        refreshToken,
        "Bearer", // Convert to seconds
        new UserInfo(customer.getId(), customer.getEmail(), customer.getName(), customer.getAvatarUrl()));
  }
}
