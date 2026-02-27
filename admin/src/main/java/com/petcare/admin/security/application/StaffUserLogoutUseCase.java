package com.petcare.admin.security.application;

import com.petcare.admin.security.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class StaffUserLogoutUseCase {

  private final RefreshTokenRepository refreshTokenRepository;
  private final RedisTemplate<String, String> redisTemplate;

  public void execute(String refreshToken) {
    refreshTokenRepository.revoke(refreshToken);
    redisTemplate.delete("refresh:" + refreshToken);
  }
}
