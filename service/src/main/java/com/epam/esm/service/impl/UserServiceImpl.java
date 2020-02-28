package com.epam.esm.service.impl;

import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.security.SecurityUserDetails;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The type User service.
 */
@Service
public class UserServiceImpl implements UserService {

  private final UserMapper userMapper;

  /**
   * Instantiates a new Tag service.
   *
   * @param userMapper the user mapper
   */
  @Autowired
  public UserServiceImpl(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public User create(final User user) {
    try {
      prepareUserForInsert(user);
      userMapper.insert(user);
      return user;
    } catch (DataIntegrityViolationException e) {
      throw new ServerException(ExceptionType.INCORRECT_INPUT_DATA);
    }
  }

  private void prepareUserForInsert(User user) {
    user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
    user.setRole(Role.USER);
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    return new SecurityUserDetails(userMapper.selectByUsername(username)
        .orElseThrow(() -> new ServerException(ExceptionType.AUTHENTICATION_FAILURE)));
  }
}