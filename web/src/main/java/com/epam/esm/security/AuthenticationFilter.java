package com.epam.esm.security;

import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

  private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
  private final TokenService tokenService;
  private final UserDetailsService userDetailsService;
  private final String authenticationTokenKey;

  public AuthenticationFilter(TokenService tokenService,
      @Qualifier("securityUserDetailsServer") UserDetailsService userDetailsService) {
    this.tokenService = tokenService;
    this.userDetailsService = userDetailsService;
    this.authenticationTokenKey = UUID.randomUUID().toString();
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    String token = request.getHeader(AUTHORIZATION_HEADER_NAME);
    if (token != null && !token.isEmpty() && tokenService.isValidToken(token)) {
      SecurityContextHolder.getContext().setAuthentication(getAuthenticationForUserWithExistedToken(token));
    } else {
      SecurityContextHolder.getContext().setAuthentication(getAuthenticationForGuest());
    }
    filterChain.doFilter(request, response);
  }

  public Authentication getAuthenticationForUserWithExistedToken(String token) {
    UserDetails userDetails =
        userDetailsService.loadUserByUsername(tokenService.extractUserFromToken(token).getUsername());
    return new UsernamePasswordAuthenticationToken(userDetails, null,
        userDetails.getAuthorities());
  }

  public Authentication getAuthenticationForGuest() {
    return new AnonymousAuthenticationToken(authenticationTokenKey,
        new SecurityUserDetails(new User()),
        AuthorityUtils.createAuthorityList(Role.GUEST.getRoleName()));
  }
}