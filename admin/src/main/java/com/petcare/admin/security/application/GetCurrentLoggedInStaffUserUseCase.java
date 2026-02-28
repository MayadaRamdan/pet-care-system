package com.petcare.admin.security.application;

import com.petcare.admin.staffuser.domain.StaffUser;
import com.petcare.admin.staffuser.repository.StaffUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class GetCurrentLoggedInStaffUserUseCase {

  private final StaffUserRepository staffUserRepository;

  public StaffUser execute() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated()) {
      return null;
    }

    return staffUserRepository.findByUsername(auth.getName()).orElse(null);
  }
}
