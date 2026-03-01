package com.petcare.admin.security.domain;

import com.petcare.admin.staffuser.domain.StaffUser;
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
@Table(name = "staff_user_security_token")
public class SecurityToken {

  @Id private String id; // UUID

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "staff_user_id", nullable = false)
  private StaffUser staffUser;

  @Column(columnDefinition = "TEXT", unique = true, nullable = false)
  private String accessToken;

  private Instant accessTokenCreatedAt;
  private Instant accessTokenExpiresAt;

  private String deviceInfo; // User-Agent or device identifier
  private String ipAddress;

  private boolean revoked;
  private Instant revokedAt;

  public SecurityToken() {
    accessTokenCreatedAt = Instant.now();
    revoked = false;
  }
}
