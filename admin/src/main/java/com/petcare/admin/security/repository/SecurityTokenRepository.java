package com.petcare.admin.security.repository;

import com.petcare.admin.security.domain.SecurityToken;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityTokenRepository extends JpaRepository<SecurityToken, String> {

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("UPDATE SecurityToken t SET t.revoked= true, t.revokedAt= :instant WHERE t.id= :tokenId")
  void revoke(String tokenId, Instant instant);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "UPDATE SecurityToken t SET t.revoked= true, t.revokedAt= :instant WHERE t.staffUser.id= :userId")
  void revokeAll(Long userId, Instant instant);

  @Query(
      "SELECT t FROM SecurityToken t"
          + " left join fetch t.staffUser au"
          + " left join fetch au.role"
          + " WHERE t.id= :tokenId and t.revoked= false")
  Optional<SecurityToken> fetchFullAccessToken(String tokenId);
}
