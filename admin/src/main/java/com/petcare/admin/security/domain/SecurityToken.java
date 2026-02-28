package com.petcare.admin.security.domain;

import com.petcare.admin.staffuser.domain.StaffUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "staff_user_security_tokens")
public class SecurityToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "staff_user_id", nullable = false)
  private StaffUser staffUser;

  private String deviceInfo; // User-Agent or device identifier
  private String ipAddress;

  @Column(unique = true, nullable = false)
  private String accessToken;

  private Instant accessTokenCreatedAt;
  private Instant accessTokenExpiresAt;

  @Column(unique = true, nullable = false)
  private String refreshToken; // UUID

  private Instant refreshTokenCreatedAt;
  private Instant refreshTokenExpiresAt;

  private boolean revoked;

  public SecurityToken() {
    accessTokenCreatedAt = Instant.now();
    refreshTokenCreatedAt = Instant.now();
    revoked = false;
  }
}
