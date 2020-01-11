package com.epam.esm.pool;

import java.sql.Connection;

/**
 * The interface Connection pool for creating and managing database connections. There should be the one instance of
 * this interface. It should support lazy concurrent loading.
 */
public interface ConnectionPool {


  /**
   * Gets connection.
   *
   * @return the connection
   */
  Connection getConnection();


  /**
   * Release connection.
   *
   * @param connection the connection
   */
  void releaseConnection(Connection connection);
}