package com.petcare.admin.security.application;

import com.petcare.admin.staffuser.domain.StaffUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.access-token-expiration}")
  private Long accessTokenExpiration;

  private SecretKey key;

  @PostConstruct
  public void init() {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateAccessToken(StaffUser user) {

    return Jwts.builder()
        .subject(user.getId().toString())
        .claim("role", user.getRole().getName())
        .expiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
        .signWith(key)
        .compact();
  }
}
