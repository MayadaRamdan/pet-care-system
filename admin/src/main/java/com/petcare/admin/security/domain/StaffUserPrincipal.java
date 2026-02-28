package com.petcare.admin.security.domain;

import com.petcare.admin.staffuser.domain.StaffUser;
import java.util.Collection;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
@Getter
public class StaffUserPrincipal implements UserDetails {

  private final StaffUser user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // All customers have the same basic authority
    return Collections.singleton(new SimpleGrantedAuthority("CUSTOMER"));
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return !user.isDeleted();
  }

  @Override
  public boolean isAccountNonLocked() {
    return !user.isDeleted();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return user.isActive();
  }

  public Long getId() {
    return user.getId();
  }
}
