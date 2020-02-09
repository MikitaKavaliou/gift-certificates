package com.epam.esm.exception;

import com.epam.esm.entity.ServiceErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * The type Exception handling controller. Controller class used for exception handling and generating response.
 */
@ControllerAdvice
public class ServiceExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServiceExceptionHandler.class);

  /**
   * Handle no handler found exception service response.
   *
   * @param ex the ex
   * @return the service response
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  public @ResponseBody
  ResponseEntity<ServiceErrorResponse> handleNoHandlerFoundException(Exception ex) {
    return createServiceResponse(ex, ExceptionType.NOT_FOUND_HANDLER);
  }

  /**
   * Handle http message not readable exception service response.
   *
   * @param ex the ex
   * @return the service response
   */
  @ExceptionHandler({HttpMessageNotReadableException.class})
  public @ResponseBody
  ResponseEntity<ServiceErrorResponse> handleHttpMessageNotReadableException(Exception ex) {
    return createServiceResponse(ex, ExceptionType.MESSAGE_NOT_READABLE);
  }

  /**
   * Handle method argument type mismatch service response.
   *
   * @param ex the ex
   * @return the service response
   */
  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public @ResponseBody
  ResponseEntity<ServiceErrorResponse> handleMethodArgumentTypeMismatch(Exception ex) {
    return createServiceResponse(ex, ExceptionType.METHOD_ARGUMENT_TYPE_MISMATCH);
  }

  /**
   * Handle server exception service response.
   *
   * @param ex the ex
   * @return the service response
   */
  @ExceptionHandler({ServerException.class})
  public @ResponseBody
  ResponseEntity<ServiceErrorResponse> handleServerException(ServerException ex) {
    return createServiceResponse(ex, ex.getExceptionType());
  }

  /**
   * Handle internal server error service response.
   *
   * @param ex the ex
   * @return the service response
   */
  @ExceptionHandler(Throwable.class)
  public @ResponseBody
  ResponseEntity<ServiceErrorResponse> handleInternalServerError(Throwable ex) {
    return createServiceResponse(ex, ExceptionType.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<ServiceErrorResponse> createServiceResponse(Throwable ex, ExceptionType exceptionType) {
    String exceptionMessage = ex instanceof ServerException ? exceptionType.getMessage() : ex.getMessage();
    LOGGER.error(exceptionMessage);
    return new ResponseEntity<>(
        new ServiceErrorResponse(exceptionType.getServerErrorCode(), exceptionType.getMessage()),
        HttpStatus.valueOf(exceptionType.getHttpErrorCode()));
  }
}