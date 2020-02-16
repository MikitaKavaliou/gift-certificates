package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserDao userDao;

  /**
   * Instantiates a new Tag service.
   *
   * @param userDao the tag dao
   */
  @Autowired
  public UserServiceImpl(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public User create(final User user) {
    prepareUserForInsert(user);
    userDao.findByUsername(user.getUsername()).ifPresentOrElse(u -> {
      throw new ServerException(ExceptionType.USER_ALREADY_EXISTS);
    }, () -> setNewUserFields(user, userDao.findById(userDao.create(user))
        .orElseThrow(() -> new ServerException(ExceptionType.ERROR_CREATING_ENTITY))));
    return user;
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
    return userDao.findByUsername(username)
        .orElseThrow(() -> new ServerException(ExceptionType.AUTHENTICATION_FAILURE));
  }
}