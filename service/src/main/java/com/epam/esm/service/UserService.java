package com.epam.esm.service;

import com.epam.esm.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * The interface User service.
 */
public interface UserService extends UserDetailsService {

  /**
   * Create user, returns created user.
   *
   * @param user the user
   * @return created user
   */
  User create(User user);
}