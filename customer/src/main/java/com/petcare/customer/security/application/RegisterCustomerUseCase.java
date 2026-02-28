package com.petcare.customer.security.application;

import com.petcare.common.exception.domain.BadRequestException;
import com.petcare.common.security.domain.DeviceTrackingInfo;
import com.petcare.customer.customer.domain.Customer;
import com.petcare.customer.customer.repository.CustomerRepository;
import com.petcare.customer.security.dto.AuthResponse;
import com.petcare.customer.security.dto.CustomerRegisterRequest;
import com.petcare.customer.security.dto.UserInfo;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class RegisterCustomerUseCase {

  private final CustomerRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final CreateSecurityTokenUseCase createSecurityTokenUseCase;

  public AuthResponse execute(
      CustomerRegisterRequest request, DeviceTrackingInfo deviceTrackingInfo) {
    // Check if username exists
    if (userRepository.existsByEmail(request.email())) {
      throw new BadRequestException("Username is already taken");
    }

    // Check if email exists
    if (userRepository.existsByEmail(request.email())) {
      throw new BadRequestException("Email is already registered");
    }

    // Create user (NO ROLES!)
    Customer user = new Customer();
    user.setEmail(request.email());
    user.setPassword(passwordEncoder.encode(request.password()));
    user.setName(request.name());
    user.setActive(true);
    user.setDeleted(false);
    user.setEmailVerified(false);

    user = userRepository.save(user);

    log.info("New customer registered: {}", user.getEmail());

    // Auto-login after registration
    String accessToken = jwtService.generateAccessToken(user);

    String refreshToken = UUID.randomUUID().toString();

    createSecurityTokenUseCase.execute(user, accessToken, refreshToken, deviceTrackingInfo);

    return new AuthResponse(
        accessToken,
        refreshToken,
        "Bearer", // Convert to seconds
        new UserInfo(user.getId(), user.getEmail(), user.getName(), user.getAvatarUrl()));
  }
}
