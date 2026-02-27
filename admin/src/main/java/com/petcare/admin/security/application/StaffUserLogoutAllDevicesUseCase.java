package com.petcare.admin.security.application;

import com.petcare.admin.security.domain.RefreshToken;
import com.petcare.admin.security.repository.RefreshTokenRepository;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class StaffUserLogoutAllDevicesUseCase {

  private final RefreshTokenRepository refreshTokenRepository;
  private final RedisTemplate<String, String> redisTemplate;

  public void execute(Long staffUserId) {

    Set<RefreshToken> tokens = refreshTokenRepository.findAllByStaffUserId(staffUserId);

    for (RefreshToken t : tokens) {
      redisTemplate.delete("refresh:" + t.getToken());
    }

    refreshTokenRepository.revokeAll(staffUserId);
  }
}
