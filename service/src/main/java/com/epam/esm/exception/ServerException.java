package com.epam.esm.exception;

public class ServerException extends RuntimeException {

  private final ExceptionType exceptionType;

  public ServerException(ExceptionType exceptionType) {
    this.exceptionType = exceptionType;
  }

  public ExceptionType getExceptionType() {
    return exceptionType;
  }
}