package com.petcare.common.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class Auditable {

  @Column(nullable = false)
  protected boolean active;

  @Column(nullable = false)
  protected boolean deleted;

  @CreatedDate
  @Column(updatable = false)
  private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;

  @CreatedBy private String createdBy;

  @LastModifiedBy private String updatedBy;

  private Instant deletedAt;

  @Version private Long version;
}
