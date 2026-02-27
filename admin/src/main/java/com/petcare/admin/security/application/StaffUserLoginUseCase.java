package com.petcare.admin.security.application;

import com.petcare.admin.security.domain.RefreshToken;
import com.petcare.admin.security.domain.StaffUserRole;
import com.petcare.admin.security.dto.AuthResponse;
import com.petcare.admin.security.dto.UserInfo;
import com.petcare.admin.security.repository.RefreshTokenRepository;
import com.petcare.admin.staffuser.domain.StaffUser;
import com.petcare.admin.staffuser.repository.StaffUserRepository;
import com.petcare.admin.utils.HttpServletRequestUtils;
import com.petcare.common.security.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class StaffUserLoginUseCase {

  private final RefreshTokenRepository refreshTokenRepository;

  private final StaffUserRepository userRepo;
  private final JwtService jwtService;
  private final RedisTemplate<String, String> redisTemplate;
  private final PasswordEncoder encoder;

  public AuthResponse execute(LoginRequest request, HttpServletRequest httpRequest) {

    StaffUser staffUser = userRepo.findByUsername(request.username()).orElseThrow();

    if (!encoder.matches(request.password(), staffUser.getPassword()))
      throw new RuntimeException("Invalid password");

    String accessToken = jwtService.generateAccessToken(staffUser);

    String refreshTokenStr = UUID.randomUUID().toString();

    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setToken(refreshTokenStr);
    refreshToken.setStaffUser(staffUser);
    refreshToken.setExpiryDate(Instant.now().plus(30, ChronoUnit.DAYS));
    refreshToken.setDeviceInfo(HttpServletRequestUtils.getDeviceInfo(httpRequest));
    refreshToken.setIpAddress(HttpServletRequestUtils.getClientIp(httpRequest));

    refreshTokenRepository.save(refreshToken);

    redisTemplate
        .opsForValue()
        .set("access:" + accessToken, staffUser.getId().toString(), Duration.ofMinutes(15));

    redisTemplate.opsForValue().set("refresh:" + refreshTokenStr, "1", Duration.ofDays(30));

    StaffUserRole role = staffUser.getRole();
    UserInfo userInfo =
        new UserInfo(
            staffUser.getId(),
            staffUser.getUsername(),
            staffUser.getName(),
            staffUser.getEmail(),
            role.getName(),
            role.getPermissions());

    return new AuthResponse(accessToken, refreshTokenStr, "Bearer", null, userInfo);
  }
}
