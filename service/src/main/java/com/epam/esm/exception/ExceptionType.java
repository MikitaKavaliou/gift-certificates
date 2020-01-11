package com.epam.esm.exception;

public enum ExceptionType {
  RESOURCE_NOT_FOUND("Resource with requested id not found", 404, 40401),
  NOT_FOUND_HANDLER("Resource not found", 404, 40402),
  INTERNAL_SERVER_ERROR("Internal server error", 500, 50001),
  MESSAGE_NOT_READABLE("Message not readable", 400, 40001),
  METHOD_ARGUMENT_TYPE_MISMATCH("Method argument type mismatch", 400, 40002);

  private String message;
  private int httpErrorCode;
  private int serverErrorCode;

  ExceptionType(String message, int httpErrorCode, int serverErrorCode) {
    this.message = message;
    this.httpErrorCode = httpErrorCode;
    this.serverErrorCode = serverErrorCode;
  }

  public String getMessage() {
    return message;
  }

  public int getHttpErrorCode() {
    return httpErrorCode;
  }

  public int getServerErrorCode() {
    return serverErrorCode;
  }
}