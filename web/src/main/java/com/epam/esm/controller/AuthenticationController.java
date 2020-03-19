package com.epam.esm.controller;

import com.epam.esm.dto.TokenDto;
import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.model.User;
import com.epam.esm.security.TokenService;
import com.epam.esm.service.UserService;
import com.epam.esm.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Authentication controller.
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes =
    MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;
  private final UserService userService;

  /**
   * Instantiates a new Authentication controller.
   *
   * @param authenticationManager the authentication manager
   * @param tokenService          the token service
   * @param userService           the user service
   */
  @Autowired
  public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService,
      UserService userService) {
    this.authenticationManager = authenticationManager;
    this.tokenService = tokenService;
    this.userService = userService;
  }

  /**
   * Login response entity.
   *
   * @param user the user
   * @return the response entity
   */
  @Secured("ROLE_GUEST")
  @PostMapping(value = "/login")
  public ResponseEntity<TokenDto> login(@RequestBody User user) {
    validateUser(user);
    User authenticatedUser = (User) authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())).getPrincipal();
    return ResponseEntity
        .status(HttpStatus.OK.value())
        .body(tokenService.createToken(authenticatedUser));
  }

  /**
   * Sign up response entity.
   *
   * @param user the user
   * @return the response entity
   */
  @Secured("ROLE_GUEST")
  @PostMapping(value = "/signup")
  public ResponseEntity<TokenDto> signUp(@RequestBody User user) {
    validateUser(user);
    return ResponseEntity
        .status(HttpStatus.CREATED.value())
        .body(tokenService.createToken(userService.create(user)));
  }

  /**
   * Validate admin token response entity.
   *
   * @return the response entity
   */
  @Secured("ROLE_ADMIN")
  @PostMapping(value = "token", params = "admin")
  public ResponseEntity<Void> validateAdminToken() {
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  /**
   * Validate user.
   *
   * @param user the user
   */
  private void validateUser(User user) {
    if (!UserValidator.isValidUser(user)) {
      throw new ServerException(ExceptionType.INCORRECT_INPUT_DATA);
    }
  }
}