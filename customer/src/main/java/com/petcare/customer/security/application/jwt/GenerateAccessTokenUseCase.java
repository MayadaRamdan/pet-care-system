package com.petcare.customer.security.application.jwt;

import com.petcare.common.security.domain.DeviceTrackingInfo;
import com.petcare.customer.customer.domain.Customer;
import com.petcare.customer.security.application.CreateSecurityTokenUseCase;
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

  public String execute(Customer customer, DeviceTrackingInfo deviceTrackingInfo) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", customer.getId());
    claims.put("email", customer.getEmail());

    String accessToken =
        Jwts.builder()
            .subject(customer.getEmail())
            .claims(claims)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + (60 * 1000 * accessTokenExpiration)))
            .signWith(key)
            .compact();

    String tokenId = UUID.randomUUID().toString();
    createSecurityTokenUseCase.execute(tokenId, accessToken, customer, deviceTrackingInfo);

    return tokenId;
  }
}
