package com.epam.esm.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenService {

  @Value("${token.signature}")
  private String signature;

  @Value("${token.validity.duration}")
  private long validityDuration;

  @Value("${spring.application.name}")
  private String tokenIssuer;

  public String createToken(User user) {
    Date tokenCreationDate = new Date();
    Algorithm algorithm = Algorithm.HMAC256(signature);
    return JWT.create()
        .withClaim("id", user.getId())
        .withClaim("username", user.getUsername())
        .withClaim("role", user.getRole().toString())
        .withIssuedAt(tokenCreationDate)
        .withExpiresAt(new Date(tokenCreationDate.getTime() + validityDuration))
        .withIssuer(tokenIssuer)
        .sign(algorithm);
  }

  public User extractUserFromToken(String token) {
    DecodedJWT jwt = JWT.decode(token);
    return new User(jwt.getClaim("id").asLong(), jwt.getClaim("username").asString(),
        Role.valueOf(jwt.getClaim("role").asString()));
  }

  public boolean isValidToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(signature);
      JWTVerifier jwtVerifier = JWT.require(algorithm).build();
      jwtVerifier.verify(token);
      return true;
    } catch (JWTVerificationException ex) {
      return false;
    }
  }
}