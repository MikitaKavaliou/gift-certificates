package com.epam.esm.service;

import com.epam.esm.model.User;

public interface UserService {

  User create(User user);

  User findByUsername(String username);
}