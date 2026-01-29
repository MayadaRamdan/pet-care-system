package com.petcare.common.common.ormlistener;

import com.petcare.common.common.domain.Auditable;
import jakarta.persistence.PreRemove;
import java.time.Instant;

public class SoftDeleteListener {

  @PreRemove
  public void preRemove(Object entity) {

    if (entity instanceof Auditable auditableEntity) {
      auditableEntity.setDeletedAt(Instant.now());
      auditableEntity.setActive(false);
      auditableEntity.setDeleted(true);
    }
  }
}
