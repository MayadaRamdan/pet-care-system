package com.petcare.customer.redis.application;

import com.petcare.customer.redis.domain.RefreshToken;
import com.petcare.customer.redis.repository.RefreshTokenRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;

  @Value("${jwt.refresh-token-expiration}")
  private Long refreshTokenExpiration;

  public RefreshToken createRefreshToken(Long userId, String deviceInfo, String ipAddress) {
    String token = UUID.randomUUID().toString();

    RefreshToken refreshToken =
        new RefreshToken(
            token,
            userId,
            deviceInfo,
            ipAddress,
            LocalDateTime.now(),
            refreshTokenExpiration / 1000 // Convert to seconds for Redis TTL
            );

    return refreshTokenRepository.save(refreshToken);
  }

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public void deleteByToken(String token) {
    refreshTokenRepository.deleteById(token);
  }

  public void deleteAllByUserId(Long userId) {
    refreshTokenRepository.deleteByUserId(userId);
  }

  public List<RefreshToken> findAllByUserId(Long userId) {
    return refreshTokenRepository.findByUserId(userId);
  }

  public boolean validateRefreshToken(String token) {
    return refreshTokenRepository.findByToken(token).isPresent();
  }
}
