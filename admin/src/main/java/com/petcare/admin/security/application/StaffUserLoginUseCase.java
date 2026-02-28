package com.petcare.admin.security.application;

import com.petcare.admin.security.domain.StaffUserRole;
import com.petcare.admin.security.dto.AuthResponse;
import com.petcare.admin.security.dto.StaffUserInfo;
import com.petcare.admin.staffuser.domain.StaffUser;
import com.petcare.admin.staffuser.repository.StaffUserRepository;
import com.petcare.common.security.domain.DeviceTrackingInfo;
import com.petcare.common.security.dto.LoginRequest;
import java.util.UUID;
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
  private final JwtService jwtService;
  private final PasswordEncoder encoder;
  private final CreateSecurityTokenUseCase createSecurityTokenUseCase;

  public AuthResponse execute(LoginRequest request, DeviceTrackingInfo deviceTrackingInfo) {

    StaffUser staffUser =
        userRepository
            .findByUsername(request.username())
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found: " + request.username()));

    if (!encoder.matches(request.password(), staffUser.getPassword()))
      throw new RuntimeException("Invalid password");

    String accessToken = jwtService.generateAccessToken(staffUser);
    String refreshToken = UUID.randomUUID().toString();

    createSecurityTokenUseCase.execute(staffUser, accessToken, refreshToken, deviceTrackingInfo);

    StaffUserRole role = staffUser.getRole();
    StaffUserInfo staffUserInfo =
        new StaffUserInfo(
            staffUser.getId(),
            staffUser.getUsername(),
            staffUser.getName(),
            staffUser.getEmail(),
            role.getName(),
            role.getPermissions());

    return new AuthResponse(accessToken, refreshToken, "Bearer", staffUserInfo);
  }
}
