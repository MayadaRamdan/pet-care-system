package com.petcare.customer.redis.repository;

import com.petcare.customer.redis.domain.RefreshToken;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
  List<RefreshToken> findByUserId(Long userId);

  void deleteByUserId(Long userId);

  Optional<RefreshToken> findByToken(String token);
}
