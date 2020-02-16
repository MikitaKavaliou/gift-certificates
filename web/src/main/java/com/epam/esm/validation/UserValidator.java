package com.epam.esm.validation;

import com.epam.esm.model.User;

public class UserValidator {

  private static final int MAX_USERNAME_LENGTH = 20;
  private static final int MAX_PASSWORD_LENGTH = 20;

  private UserValidator() {

  }

  public static boolean isValidUser(User user) {
    return user.getPassword() != null && user.getPassword().length() <= MAX_PASSWORD_LENGTH &&
        user.getUsername() != null && user.getUsername().length() <= MAX_USERNAME_LENGTH;
  }
}