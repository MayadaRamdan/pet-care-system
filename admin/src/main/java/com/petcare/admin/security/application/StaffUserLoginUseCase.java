package com.petcare.admin.security.application;

import com.petcare.admin.security.domain.StaffUserRole;
import com.petcare.admin.security.dto.AuthResponse;
import com.petcare.admin.security.dto.StaffUserInfo;
import com.petcare.admin.staffuser.domain.StaffUser;
import com.petcare.admin.staffuser.repository.StaffUserRepository;
import com.petcare.common.security.domain.DeviceTrackingInfo;
import com.petcare.common.security.dto.LoginRequest;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class StaffUserLoginUseCase {

  private final StaffUserRepository userRepository;
  private final GenerateAccessTokenUseCase generateAccessTokenUseCase;
  private final PasswordEncoder encoder;

  public AuthResponse execute(LoginRequest request, DeviceTrackingInfo deviceTrackingInfo) {
    StaffUser staffUser =
        userRepository
            .findByUsername(request.username())
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found: " + request.username()));

    if (!encoder.matches(request.password(), staffUser.getPassword()))
      throw new RuntimeException("Invalid password");

    String accessToken = generateAccessTokenUseCase.execute(staffUser, deviceTrackingInfo);

    StaffUserRole role = staffUser.getRole();
    StaffUserInfo staffUserInfo =
        new StaffUserInfo(
            staffUser.getId(),
            staffUser.getUsername(),
            staffUser.getName(),
            staffUser.getEmail(),
            role.getName(),
            Collections.emptySet());

    return new AuthResponse(accessToken, "Bearer", staffUserInfo);
  }
}
