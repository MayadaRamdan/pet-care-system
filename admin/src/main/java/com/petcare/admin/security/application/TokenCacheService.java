package com.petcare.admin.security.application;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenCacheService {

  private final RedisTemplate<String, String> redisTemplate;
  private static final String PREFIX = "petcare:jwt:";

  // Cache token with TTL matching JWT expiry
  public void cacheToken(String token, Long userId, long ttlMillis) {
    redisTemplate
        .opsForValue()
        .set(PREFIX + token, String.valueOf(userId), ttlMillis, TimeUnit.MILLISECONDS);
  }

  public boolean isTokenCached(String token) {
    return redisTemplate.hasKey(PREFIX + token);
  }

  public void evictToken(String token) {
    redisTemplate.delete(PREFIX + token);
  }
}
