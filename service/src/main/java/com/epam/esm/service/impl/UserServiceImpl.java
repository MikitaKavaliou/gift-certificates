package com.epam.esm.service.impl;

import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserMapper userMapper;

  /**
   * Instantiates a new Tag service.
   */
  @Autowired
  public UserServiceImpl(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public User create(final User user) {
    prepareUserForInsert(user);
    userMapper.selectByUsername(user.getUsername()).ifPresentOrElse(u -> {
      throw new ServerException(ExceptionType.USER_ALREADY_EXISTS);
    }, () -> setNewUserFields(user, userMapper.selectById(insertUser(user))
        .orElseThrow(() -> new ServerException(ExceptionType.ERROR_CREATING_ENTITY))));
    return user;
  }

  private Long insertUser(User user) {
    userMapper.insert(user);
    return user.getId();
  }

  private void prepareUserForInsert(User user) {
    user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
    user.setRole(Role.USER);
  }

  private void setNewUserFields(User user, User newUser) {
    user.setId(newUser.getId());
    user.setUsername(newUser.getUsername());
    user.setPassword(newUser.getPassword());
    user.setRole(newUser.getRole());
  }

  @Override
  public User findByUsername(String username) {
    return userMapper.selectByUsername(username)
        .orElseThrow(() -> new ServerException(ExceptionType.AUTHENTICATION_FAILURE));
  }
}