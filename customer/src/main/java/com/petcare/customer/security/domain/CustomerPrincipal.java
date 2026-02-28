package com.petcare.customer.security.domain;

import com.petcare.customer.customer.domain.Customer;
import java.util.Collection;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
@Getter
public class CustomerPrincipal implements UserDetails {

  @Getter private final Customer customer;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // All customers have the same basic authority
    return Collections.singleton(new SimpleGrantedAuthority("CUSTOMER"));
  }

  @Override
  public String getPassword() {
    return customer.getPassword();
  }

  @Override
  public String getUsername() {
    return customer.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return !customer.isDeleted();
  }

  @Override
  public boolean isAccountNonLocked() {
    return !customer.isDeleted();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return customer.isActive();
  }

  public Long getId() {
    return customer.getId();
  }
}
