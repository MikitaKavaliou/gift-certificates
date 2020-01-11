package com.epam.esm.pool.impl;


import com.epam.esm.exception.ConnectionPoolException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


/**
 * The type Connection pool configurer. Initializing values from properties, registering drivers, creating connections.
 */
@Component
@PropertySource("classpath:database.properties")
public class ConnectionPoolConfigurer {

  private String databaseLogin;
  private String databasePassword;
  private String databaseUrl;
  private String driverName;
  private int poolSize;
  private int checkDelay;

  @Value("${db.url}")
  private void setDatabaseUrl(String databaseUrl) {
    this.databaseUrl = databaseUrl;
  }

  @Value("${db.login}")
  private void setDatabaseLogin(String databaseLogin) {
    this.databaseLogin = databaseLogin;
  }

  @Value("${db.password}")
  private void setDatabasePassword(String databasePassword) {
    this.databasePassword = databasePassword;
  }

  @Value("${db.driver}")
  private void setDriverName(String driverName) {
    this.driverName = driverName;
  }

  public int getPoolSize() {
    return poolSize;
  }

  @Value("${pool.size}")
  public void setPoolSize(int poolSize) {
    this.poolSize = poolSize;
  }

  public int getCheckDelay() {
    return checkDelay;
  }

  @Value("${pool.delay}")
  public void setCheckDelay(int checkDelay) {
    this.checkDelay = checkDelay;
  }

  /**
   * Register driver.
   */
  @PostConstruct
  public void registerDriver() {
    try {
      Class.forName(driverName);
    } catch (ClassNotFoundException e) {
      throw new ConnectionPoolException("Error registering database driver", e);
    }
  }

  /**
   * Create database connection.
   *
   * @return the database connection
   * @throws SQLException the sql exception
   */

  public Connection createConnection() throws SQLException {
    return DriverManager.getConnection(databaseUrl, databaseLogin, databasePassword);
  }
}