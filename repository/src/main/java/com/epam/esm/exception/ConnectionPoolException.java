package com.epam.esm.exception;

public class ConnectionPoolException extends RuntimeException {

  public ConnectionPoolException(String reason, Throwable cause) {
    super(reason, cause);
  }
}