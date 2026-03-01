package com.petcare.customer.security.domain;

import com.petcare.customer.customer.domain.Customer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customer_security_token")
public class SecurityToken {

  @Id private String id; // UUID

  @Column(columnDefinition = "TEXT", unique = true, nullable = false)
  private String accessToken;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  private String deviceInfo; // User-Agent or device identifier
  private String ipAddress;

  private Instant accessTokenCreatedAt;
  private Instant accessTokenExpiresAt;

  private boolean revoked;
  private Instant revokedAt;

  public SecurityToken() {
    accessTokenCreatedAt = Instant.now();
    revoked = false;
  }
}
