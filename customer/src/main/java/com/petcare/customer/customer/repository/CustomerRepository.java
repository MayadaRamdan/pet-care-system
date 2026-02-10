package com.petcare.customer.customer.repository;

import com.petcare.customer.customer.domain.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
  Optional<Customer> findByUsername(String username);

  Optional<Customer> findByEmail(String email);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
}
