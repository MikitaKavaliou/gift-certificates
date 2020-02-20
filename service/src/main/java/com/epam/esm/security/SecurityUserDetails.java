package com.epam.esm.security;


import com.epam.esm.model.User;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUserDetails extends User implements UserDetails {

  public SecurityUserDetails() {
  }

  public SecurityUserDetails(User user) {
    super(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections
        .singletonList(new SimpleGrantedAuthority(this.getRole().getRoleName()));
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}