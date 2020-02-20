package com.epam.esm.security;

import com.epam.esm.model.User;
import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class TokenAuthentication implements Authentication {

  private UserDetails userDetails;
  private boolean isAuthenticated;
  private Collection<? extends GrantedAuthority> authorities;

  public TokenAuthentication(User user) {
    userDetails = new SecurityUserDetails(user);
    authorities = userDetails.getAuthorities();
    isAuthenticated = true;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getDetails() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return userDetails;
  }

  @Override
  public boolean isAuthenticated() {
    return isAuthenticated;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) {
    this.isAuthenticated = isAuthenticated;
  }

  @Override
  public String getName() {
    return userDetails != null ? userDetails.getUsername() : null;
  }
}