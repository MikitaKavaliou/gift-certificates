package com.epam.esm.security;

import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

  private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
  private final TokenService tokenService;

  public AuthenticationFilter(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    String token = request.getHeader(AUTHORIZATION_HEADER_NAME);
    if (tokenService.isValidToken(token)) {
      SecurityContextHolder.getContext().setAuthentication(getAuthenticationForUserWithExistedToken(token));
    } else {
      SecurityContextHolder.getContext().setAuthentication(getAuthenticationForGuest());
    }
    filterChain.doFilter(request, response);
  }

  public Authentication getAuthenticationForUserWithExistedToken(String token) {
    return new TokenAuthentication(tokenService.extractUserFromToken(token));
  }

  public Authentication getAuthenticationForGuest() {
    return new TokenAuthentication(new User(Role.GUEST));
  }
}