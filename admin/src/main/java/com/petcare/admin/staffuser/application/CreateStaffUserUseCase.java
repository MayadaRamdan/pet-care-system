package com.petcare.admin.staffuser.application;

import com.petcare.admin.security.repository.StaffUserRoleRepository;
import com.petcare.admin.staffuser.domain.StaffUser;
import com.petcare.admin.staffuser.domain.StaffUserCreateRequest;
import com.petcare.admin.staffuser.repository.StaffUserRepository;
import com.petcare.common.exception.domain.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@AllArgsConstructor
public class CreateStaffUserUseCase {

  private final PasswordEncoder passwordEncoder;
  private final StaffUserRepository staffUserRepository;
  private final StaffUserRoleRepository staffUserRoleRepository;

  public void execute(StaffUserCreateRequest request) {

    StaffUser staffUser = new StaffUser();
    staffUser.setUsername(request.username());
    staffUser.setPassword(passwordEncoder.encode(request.password()));
    staffUser.setEmail(request.email());
    staffUser.setName(request.name());

    staffUser.setRole(
        staffUserRoleRepository
            .findById(request.roleId())
            .orElseThrow(() -> new ResourceNotFoundException("Role", request.roleId())));

    staffUserRepository.save(staffUser);
  }
}
