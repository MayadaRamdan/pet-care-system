package com.petcare.admin.security.application;

import com.petcare.admin.security.dto.AuthResponse;
import com.petcare.admin.staffuser.application.CreateStaffUserUseCase;
import com.petcare.admin.staffuser.domain.StaffUserCreateRequest;
import com.petcare.common.security.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class StaffUserRegisterUseCase {

  private final CreateStaffUserUseCase createStaffUserUseCase;
  private final StaffUserLoginUseCase staffUserLoginUseCase;

  public AuthResponse execute(StaffUserCreateRequest request, HttpServletRequest httpRequest) {
    createStaffUserUseCase.execute(request);
    return staffUserLoginUseCase.execute(
        new LoginRequest(request.username(), request.password()), httpRequest);
  }
}
