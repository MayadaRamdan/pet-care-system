package com.petcare.admin.security.repository;

import com.petcare.admin.security.domain.RefreshToken;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

  @Query("SELECT t FROM RefreshToken t WHERE t.staffUser.id = :userId")
  Set<RefreshToken> findAllByStaffUserId(Long userId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("UPDATE RefreshToken t SET t.revoked = true WHERE t.token = :token")
  void revoke(String token);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("UPDATE RefreshToken t SET t.revoked = true WHERE t.staffUser.id = :userId")
  void revokeAll(Long userId);
}
