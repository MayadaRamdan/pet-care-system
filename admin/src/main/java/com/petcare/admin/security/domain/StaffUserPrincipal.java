package com.petcare.admin.security.domain;

import static com.petcare.common.common.utils.StringUtils.EMPTY_STRING;

import com.petcare.admin.staffuser.domain.StaffUser;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
@Getter
public class StaffUserPrincipal implements UserDetails {

  @Getter private final StaffUser user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Set<SimpleGrantedAuthority> permissions = new HashSet<>();
    if (user.getRole() != null && user.getRole().getPermissions() != null) {
      user.getRole()
          .getPermissions()
          .forEach(pr -> permissions.add(new SimpleGrantedAuthority(pr.name())));
    }
    return permissions;
  }

  @Override
  public String getPassword() {
    return EMPTY_STRING;
  }

  @Override
  public String getUsername() {
    return user.getUsername();
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
