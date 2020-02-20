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
  @Value("${token.signature}")
  private String signature;
  @Value("${token.validity.duration}")
  private long validityDuration;
  @Value("${spring.application.name}")
  private String tokenIssuer;
  private Algorithm algorithm;
  private JWTVerifier jwtVerifier;

  @PostConstruct
  public void initialize() {
    algorithm = Algorithm.HMAC256(signature);
    jwtVerifier = JWT
        .require(algorithm)
        .withIssuer(tokenIssuer)
        .build();
  }


  public TokenDto createToken(User user) {
    LocalDateTime issuedAt = LocalDateTime.now();
    LocalDateTime expiresAt = issuedAt.plusMinutes(validityDuration);
    String token = JWT.create()
        .withClaim(ID_CLAIM, user.getId())
        .withClaim(USERNAME_CLAIM, user.getUsername())
        .withClaim(ROLE_CLAIM, user.getRole().toString())
        .withIssuedAt(convertLocalDateTimeToDate(issuedAt))
        .withExpiresAt(convertLocalDateTimeToDate(expiresAt))
        .withIssuer(tokenIssuer)
        .sign(algorithm);
    return new TokenDto(token, issuedAt, expiresAt);
  }

  public User extractUserFromToken(String token) {
    DecodedJWT jwt = JWT.decode(token);
    return new User(jwt.getClaim(ID_CLAIM).asLong(), jwt.getClaim(USERNAME_CLAIM).asString(),
        Role.valueOf(jwt.getClaim(ROLE_CLAIM).asString()));
  }

  public boolean isValidToken(String token) {
    try {
      if (StringUtils.isNotBlank(token)) {
        jwtVerifier.verify(token);
        return true;
      }
      return false;
    } catch (JWTVerificationException ex) {
      return false;
    }
  }

  private Date convertLocalDateTimeToDate(LocalDateTime dateTime) {
    return Date
        .from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
  }
}