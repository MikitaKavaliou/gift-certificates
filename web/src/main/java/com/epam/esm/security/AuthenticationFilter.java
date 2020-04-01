package com.epam.esm.security;

import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

  private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
  private static final String BEARER_AUTHENTICATION_TYPE_NAME = "Bearer ";
  private static final String EMPTY_SPACE = "";
  private final TokenService tokenService;

  public AuthenticationFilter(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    String token = getTokenFromHeader(request);
    if (tokenService.isValidAcceptToken(token)) {
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

  private String getTokenFromHeader(HttpServletRequest request) {
    String headerContent = request.getHeader(AUTHORIZATION_HEADER_NAME);
    return StringUtils.isNotBlank(headerContent) ?
        headerContent.replace(BEARER_AUTHENTICATION_TYPE_NAME, EMPTY_SPACE) : null;
  }
}