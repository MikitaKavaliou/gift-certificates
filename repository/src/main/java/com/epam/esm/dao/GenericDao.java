package com.epam.esm.dao;

import java.util.Optional;

public interface GenericDao<T, K> {

  int delete(K id);

  Optional<T> findById(K id);
}