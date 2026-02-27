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
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "staff_user_refresh_tokens")
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String token; // UUID

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "staff_user_id", nullable = false)
  private StaffUser staffUser;

  private String deviceInfo; // User-Agent or device identifier

  private String ipAddress;

  private LocalDateTime createdAt;

  private Instant expiryDate;

  private boolean revoked;
}
