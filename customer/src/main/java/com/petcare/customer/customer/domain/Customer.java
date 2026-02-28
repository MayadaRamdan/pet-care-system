package com.petcare.customer.customer.domain;

import com.petcare.common.common.domain.Auditable;
import com.petcare.common.common.embeddable.Name;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "customer",
    indexes = {
      @Index(name = "idx_email", columnList = "email")
    })
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length = 100)
  private String email;

  private String password;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "first", column = @Column(name = "first_name")),
    @AttributeOverride(name = "last", column = @Column(name = "last_name"))
  })
  private Name name;

  @Column(name = "avatar_url", length = 500)
  private String avatarUrl;

  @Column(name = "last_login")
  private Instant lastLogin;

  private boolean emailVerified;
}
