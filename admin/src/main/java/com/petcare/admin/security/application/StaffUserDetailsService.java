package com.petcare.admin.security.application;

import com.petcare.admin.security.domain.StaffUserPrincipal;
import com.petcare.admin.staffuser.domain.StaffUser;
import com.petcare.admin.staffuser.repository.StaffUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StaffUserDetailsService implements UserDetailsService {

  private final StaffUserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    StaffUser user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    return new StaffUserPrincipal(user);
  }

  @Transactional(readOnly = true)
  public UserDetails loadUserById(Long id) {
    StaffUser user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    return new StaffUserPrincipal(user);
  }
}
