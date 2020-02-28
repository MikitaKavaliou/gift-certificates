package com.epam.esm.validation;

import com.epam.esm.model.User;
import org.apache.commons.lang3.StringUtils;

public class UserValidator {

  private static final int MAX_USERNAME_LENGTH = 20;
  private static final int MAX_PASSWORD_LENGTH = 20;

  private UserValidator() {

  }

  public static boolean isValidUser(User user) {
    return StringUtils.isNotBlank(user.getPassword()) && user.getPassword().length() <= MAX_PASSWORD_LENGTH &&
        StringUtils.isNotBlank(user.getUsername()) && user.getUsername().length() <= MAX_USERNAME_LENGTH;
  }
}