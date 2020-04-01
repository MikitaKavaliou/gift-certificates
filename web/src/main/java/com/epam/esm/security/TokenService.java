package com.epam.esm.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.epam.esm.dto.TokenDto;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenService {

  private static final String ID_CLAIM = "id";
  private static final String USERNAME_CLAIM = "username";
  private static final String ROLE_CLAIM = "role";
  private static final String SCOPE_CLAIM = "scope";
  private static final String ACCEPT_SCOPE = "accept";
  private static final String REFRESH_SCOPE = "refresh";
  @Value("${token.signature}")
  private String signature;
  @Value("${token.accept.validity.duration}")
  private long acceptValidityDuration;
  @Value("${token.refresh.validity.duration}")
  private long refreshValidityDuration;
  @Value("${spring.application.name}")
  private String tokenIssuer;
  private Algorithm algorithm;
  private JWTVerifier jwtAcceptVerifier;
  private JWTVerifier jwtRefreshVerifier;

  @PostConstruct
  public void initialize() {
    algorithm = Algorithm.HMAC256(signature);
    jwtAcceptVerifier = JWT
        .require(algorithm)
        .withClaim(SCOPE_CLAIM, ACCEPT_SCOPE)
        .acceptExpiresAt(0)
        .withIssuer(tokenIssuer)
        .build();
    jwtRefreshVerifier = JWT
        .require(algorithm)
        .withClaim(SCOPE_CLAIM, REFRESH_SCOPE)
        .acceptExpiresAt(0)
        .withIssuer(tokenIssuer)
        .build();
  }


  public TokenDto createTokenForUser(User user) {
    String acceptToken = createToken(user, acceptValidityDuration, ACCEPT_SCOPE);
    String refreshToken = createToken(user, refreshValidityDuration, REFRESH_SCOPE);
    return new TokenDto(acceptToken, refreshToken);
  }

  public TokenDto createTokenFromRefreshToken(String refreshToken) {
    User user = extractUserFromToken(refreshToken);
    return createTokenForUser(user);
  }

  public User extractUserFromToken(String token) {
    DecodedJWT jwt = JWT.decode(token);
    return new User(jwt.getClaim(ID_CLAIM).asLong(), jwt.getClaim(USERNAME_CLAIM).asString(),
        Role.valueOf(jwt.getClaim(ROLE_CLAIM).asString()));
  }

  public boolean isValidAcceptToken(String token) {
    try {
      if (StringUtils.isNotBlank(token)) {
        jwtAcceptVerifier.verify(token);
        return true;
      }
      return false;
    } catch (JWTVerificationException | IllegalArgumentException ex) {
      return false;
    }
  }

  public boolean isValidRefreshToken(String token) {
    try {
      if (StringUtils.isNotBlank(token)) {
        jwtRefreshVerifier.verify(token);
        return true;
      }
      return false;
    } catch (JWTVerificationException | IllegalArgumentException ex) {
      return false;
    }
  }

  private String createToken(User user, long tokenDuration, String tokenScopeValue) {
    LocalDateTime issuedAt = LocalDateTime.now();
    LocalDateTime expiresAt = issuedAt.plusMinutes(tokenDuration);
    return JWT.create()
        .withClaim(ID_CLAIM, user.getId())
        .withClaim(USERNAME_CLAIM, user.getUsername())
        .withClaim(ROLE_CLAIM, user.getRole().toString())
        .withClaim(SCOPE_CLAIM, tokenScopeValue)
        .withIssuedAt(convertLocalDateTimeToDate(issuedAt))
        .withExpiresAt(convertLocalDateTimeToDate(expiresAt))
        .withIssuer(tokenIssuer)
        .sign(algorithm);
  }

  private Date convertLocalDateTimeToDate(LocalDateTime dateTime) {
    return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
  }
}