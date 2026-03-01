package com.petcare.admin.security.repository;

import com.petcare.admin.security.domain.StaffUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffUserRoleRepository extends JpaRepository<StaffUserRole, Long> {}
