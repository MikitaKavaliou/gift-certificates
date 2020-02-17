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

@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;
  private final UserService userService;

  @Autowired
  public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService,
      UserService userService) {
    this.authenticationManager = authenticationManager;
    this.tokenService = tokenService;
    this.userService = userService;
  }

  @Secured("ROLE_GUEST")
  @PostMapping(value = "/login")
  public ResponseEntity<TokenDto> login(@RequestBody User user) {
    validateUser(user);
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),
        user.getPassword()));
    return ResponseEntity.status(HttpStatus.OK.value()).body(
        new TokenDto(tokenService.createToken(userService.findByUsername(user.getUsername()))));
  }

  @Secured("ROLE_GUEST")
  @PostMapping(value = "/signup")
  public ResponseEntity<TokenDto> signUp(@RequestBody User user) {
    validateUser(user);
    return ResponseEntity.status(HttpStatus.OK.value()).body(
        new TokenDto(tokenService.createToken(userService.create(user))));
  }

  public void validateUser(User user) {
    if (!UserValidator.isValidUser(user)) {
      throw new ServerException(ExceptionType.INCORRECT_INPUT_DATA);
    }
  }
}