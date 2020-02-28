package com.epam.esm.exception;

public class ServerException extends RuntimeException {

  private final ExceptionType exceptionType;

  public ServerException(ExceptionType exceptionType) {
    super(exceptionType.getMessage());
    this.exceptionType = exceptionType;
  }

  public ExceptionType getExceptionType() {
    return exceptionType;
  }
}