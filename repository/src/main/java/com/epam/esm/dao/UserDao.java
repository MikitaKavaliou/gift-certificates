package com.epam.esm.dao;

import com.epam.esm.model.User;
import java.util.Optional;

public interface UserDao {

  Long create(User user);

  Optional<User> findByUsername(String username);

  Optional<User> findById(Long id);
}