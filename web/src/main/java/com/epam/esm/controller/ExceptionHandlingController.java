package com.epam.esm.controller;

import com.epam.esm.builder.ServiceResponseBuilder;
import com.epam.esm.entity.ServiceResponse;
import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ExceptionHandlingController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlingController.class);

  /**
   * Handle no handler found exception service response.
   *
   * @param ex       the ex
   * @param response the response
   * @return the service response
   */
  @ExceptionHandler({NoHandlerFoundException.class})
  public @ResponseBody
  ServiceResponse<String> handleNoHandlerFoundException(Exception ex, HttpServletResponse response) {
    return createServiceResponse(ex, ExceptionType.NOT_FOUND_HANDLER, response);
  }

  /**
   * Handle http message not readable exception service response.
   *
   * @param ex       the ex
   * @param response the response
   * @return the service response
   */
  @ExceptionHandler({HttpMessageNotReadableException.class})
  ServiceResponse<String> handleHttpMessageNotReadableException(Exception ex, HttpServletResponse response) {
    return createServiceResponse(ex, ExceptionType.MESSAGE_NOT_READABLE, response);
  }

  /**
   * Handle method argument type mismatch service response.
   *
   * @param ex       the ex
   * @param response the response
   * @return the service response
   */
  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  ServiceResponse<String> handleMethodArgumentTypeMismatch(Exception ex, HttpServletResponse response) {
    return createServiceResponse(ex, ExceptionType.METHOD_ARGUMENT_TYPE_MISMATCH, response);
  }

  /**
   * Handle server exception service response.
   *
   * @param ex       the ex
   * @param response the response
   * @return the service response
   */
  @ExceptionHandler({ServerException.class})
  public @ResponseBody
  ServiceResponse<String> handleServerException(ServerException ex, HttpServletResponse response) {
    return createServiceResponse(ex, ex.getExceptionType(), response);
  }

  /**
   * Handle internal server error service response.
   *
   * @param ex       the ex
   * @param response the response
   * @return the service response
   */
  @ExceptionHandler(Throwable.class)
  public @ResponseBody
  ServiceResponse<String> handleInternalServerError(Throwable ex, HttpServletResponse response) {
    return createServiceResponse(ex, ExceptionType.INTERNAL_SERVER_ERROR, response);
  }

  private ServiceResponse<String> createServiceResponse(Throwable ex, ExceptionType exceptionType,
      HttpServletResponse response) {
    LOGGER.error(ex.getClass() + " " + ex.getMessage());
    response.setStatus(exceptionType.getHttpErrorCode());
    return new ServiceResponseBuilder<String>().
        status(String.valueOf(exceptionType.getServerErrorCode())).
        errorMessage(exceptionType.getMessage()).
        build();
  }
}