package com.petcare.customer.security.repository;

import com.petcare.customer.security.domain.SecurityToken;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityTokenRepository extends JpaRepository<SecurityToken, String> {

  @Query("SELECT t FROM SecurityToken t WHERE t.customer.id = :userId and t.revoked = false")
  Set<SecurityToken> findAllByCustomerId(Long userId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("UPDATE SecurityToken t SET t.revoked = true WHERE t.accessToken = :token")
  void revoke(String accessToken);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("UPDATE SecurityToken t SET t.revoked = true WHERE t.customer.id = :userId")
  void revokeAll(Long userId);

  @Query("SELECT t FROM SecurityToken t WHERE t.accessToken = :token and t.revoked = false")
  Optional<SecurityToken> findByAccessToken(String accessToken);
}
