package com.epam.esm.pool;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * The type Pooling data source. Pooling implementation of DataSource interface for jdbc template. Uses ConnectionPool
 * for getting database connections.
 */
@Component
public class PoolingDataSource implements DataSource {

  private final ConnectionPool connectionPool;

  /**
   * Instantiates a new Pooling data source.
   *
   * @param connectionPool the connection pool
   */
  @Autowired
  public PoolingDataSource(ConnectionPool connectionPool) {
    this.connectionPool = connectionPool;
  }

  @Override
  public Logger getParentLogger() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Connection getConnection(String username, String password) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PrintWriter getLogWriter() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setLogWriter(PrintWriter out) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getLoginTimeout() {
    return 0;
  }

  @Override
  public void setLoginTimeout(int seconds) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Connection getConnection() {
    return connectionPool.getConnection();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T unwrap(Class<T> iface) throws SQLException {
    if (iface.isInstance(this)) {
      return (T) this;
    }
    throw new SQLException("DataSource of type [" + getClass().getName() +
        "] cannot be unwrapped as [" + iface.getName() + "]");
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) {
    return iface.isInstance(this);
  }
}