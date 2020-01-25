package com.epam.esm.repository.specification;

import com.epam.esm.entity.SqlRequest;

/**
 * The interface Specification.The interface is used for creating SELECT queries to the database.
 */
public interface Specification {

  /**
   * Create sql request sql request.
   *
   * @return the sql request with sql query string and its parameters
   */
  SqlRequest createSqlRequest();
}