package com.petcare.admin.staffuser.repository;

import com.petcare.admin.staffuser.domain.StaffUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffUserRepository extends JpaRepository<StaffUser, Long> {

    Optional<StaffUser> findByUsername(String username);
}
