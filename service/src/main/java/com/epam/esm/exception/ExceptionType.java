package com.epam.esm.exception;

public enum ExceptionType {
  MESSAGE_NOT_READABLE("message.not.readable", 400, 40001),
  METHOD_ARGUMENT_TYPE_MISMATCH("argument.type.mismatch", 400, 40002),
  INCORRECT_INPUT_DATA("incorrect.input.data", 400, 40003),
  MISSING_REQUEST_PARAMETER("missing.request.parameter", 400, 40004),
  UNSATISFIED_REQUEST_PARAMETER("unsatisfied.request.parameter", 400, 40005),
  MEDIA_TYPE_NOT_SUPPORTED("media.type.not.supported", 400, 40006),
  MEDIA_TYPE_NOT_ACCEPTABLE("media.type.not.acceptable", 400, 40007),
  HTTP_REQUEST_METHOD_NOT_SUPPORTED("request.method.not.supported", 400, 40008),
  AUTHENTICATION_FAILURE("authentication.failure", 401, 40101),
  ACCESS_DENIED("access.denied", 403, 40301),
  RESOURCE_NOT_FOUND("resource.not.found", 404, 40401),
  NOT_FOUND_HANDLER("not.found.handler", 404, 40402),
  INTERNAL_SERVER_ERROR("internal.server.error", 500, 50001),
  ERROR_CREATING_ENTITY("error.creating.entity", 500, 50002);

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