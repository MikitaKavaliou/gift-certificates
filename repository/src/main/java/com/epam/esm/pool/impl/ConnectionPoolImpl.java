package com.epam.esm.pool.impl;

import com.epam.esm.exception.ConnectionPoolException;
import com.epam.esm.pool.ConnectionPool;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


/**
 * The type Connection pool implementation. Pool of database connections with check connections leak thread. Uses
 * ConnectionPoolConfigurer for initialization, registering drivers and creating connections.
 */
@Lazy
@Component
public class ConnectionPoolImpl implements ConnectionPool {

  private static final String CLOSE_CONNECTION_METHOD_NAME = "close";

  private final ConnectionPoolConfigurer connectionPoolConfigurer;
  private BlockingQueue<Connection> availableConnections;
  private BlockingQueue<Connection> givenConnections;
  private Semaphore semaphore;
  private int poolSize;

  @Autowired
  private ConnectionPoolImpl(ConnectionPoolConfigurer connectionPoolConfigurer) {
    this.connectionPoolConfigurer = connectionPoolConfigurer;
  }

  /**
   * Pool Init.
   */
  @PostConstruct
  public void init() {
    try {
      poolSize = connectionPoolConfigurer.getPoolSize();
      semaphore = new Semaphore(poolSize);
      availableConnections = new LinkedBlockingQueue<>(poolSize);
      givenConnections = new LinkedBlockingQueue<>();
      ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
      service.scheduleWithFixedDelay(new LeakCheckThread(),
          connectionPoolConfigurer.getCheckDelay(), connectionPoolConfigurer.getCheckDelay(), TimeUnit.HOURS);
      for (int number = 0; number < poolSize; number++) {
        availableConnections.put(connectionPoolConfigurer.createConnection());
      }
    } catch (SQLException e) {
      throw new ConnectionPoolException("SQLException while creating connection pool", e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ConnectionPoolException("Interrupted while creating connection pool", e);
    }
  }

  @SuppressWarnings("all")
  public Connection getConnection() {
    Connection connection = null;
    try {
      semaphore.acquire();
      connection = availableConnections.take();
      givenConnections.put(connection);
      return createProxyConnection(connection);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ConnectionPoolException("Interrupted during connection acquisition", e);
    } finally {
      semaphore.release();
    }
  }

  private Connection createProxyConnection(Connection connection) {
    return (Connection) Proxy.newProxyInstance(connection.getClass().getClassLoader(),
        new Class[]{Connection.class}, (proxy, method, args) -> {
          if (method.getName().equals(CLOSE_CONNECTION_METHOD_NAME)) {
            releaseConnection(connection);
          } else {
            return method.invoke(connection, args);
          }
          return null;
        });
  }

  public void releaseConnection(Connection connection) {
    try {
      semaphore.acquire();
      givenConnections.remove(connection);
      availableConnections.put(connection);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ConnectionPoolException("Interrupted during connection acquisition", e);
    } finally {
      semaphore.release();
    }
  }

  private void closeConnections() {
    try {
      for (int poolNumber = 0; poolNumber < poolSize; poolNumber++) {
        Connection connection = availableConnections.take();
        connection.close();
      }
    } catch (SQLException e) {
      throw new ConnectionPoolException("SQLException during closing connection", e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ConnectionPoolException("Interrupted during closing connection", e);
    }
  }

  private void deregisterDrivers() {
    try {
      Enumeration<Driver> iterator = DriverManager.getDrivers();
      while (iterator.hasMoreElements()) {
        Driver driver = iterator.nextElement();
        DriverManager.deregisterDriver(driver);
      }
    } catch (SQLException e) {
      throw new ConnectionPoolException("Interrupted during driver deregister", e);
    }
  }

  /**
   * Tear down. Closing connections, deregister drivers;
   */
  @PreDestroy
  public void tearDown() {
    closeConnections();
    deregisterDrivers();
  }

  private final class LeakCheckThread implements Runnable {

    @Override
    public void run() {
      startChecking();
      int numberOfConnections = availableConnections.size() + givenConnections.size();
      int difference = poolSize - numberOfConnections;
      if (difference > 0) {
        for (int i = 0; i < difference; i++) {
          addConnection();
        }
      }
      stopChecking();
    }

    private void addConnection() {
      Connection connection;
      try {
        connection = connectionPoolConfigurer.createConnection();
        availableConnections.put(connection);
      } catch (SQLException e) {
        throw new ConnectionPoolException("SQLException while adding connection", e);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new ConnectionPoolException("Interrupted while adding connection", e);
      }
    }

    private void startChecking() {
      semaphore.acquireUninterruptibly(poolSize);

    }

    private void stopChecking() {
      semaphore.release(poolSize);
    }
  }
}