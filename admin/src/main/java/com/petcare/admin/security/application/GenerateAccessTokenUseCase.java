package com.petcare.admin.security.application;

import com.petcare.admin.staffuser.domain.StaffUser;
import com.petcare.common.security.domain.DeviceTrackingInfo;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GenerateAccessTokenUseCase {

  @Autowired private CreateSecurityTokenUseCase createSecurityTokenUseCase;

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.access-token-expiration}")
  private Long accessTokenExpiration;

  private SecretKey key;

  @PostConstruct
  public void init() {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String execute(StaffUser user, DeviceTrackingInfo deviceTrackingInfo) {

    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", user.getId());
    claims.put("username", user.getUsername());
    claims.put("email", user.getEmail());
    claims.put("role", user.getRole().getName());
    claims.put("permissions", user.getRole().getPermissions());

    String accessToken =
        Jwts.builder()
            .subject(user.getId().toString())
            .claims(claims)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
            .signWith(key)
            .compact();

    String tokenId = UUID.randomUUID().toString();
    createSecurityTokenUseCase.execute(tokenId, accessToken, user, deviceTrackingInfo);

    return tokenId;
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      log.error("JWT validation error: {}", e.getMessage());
      return false;
    }
  }
}
