package com.epam.esm.exception;

import com.epam.esm.dto.ServerErrorDto;
import com.epam.esm.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * The type Exception handling controller. Controller class used for exception handling and generating response.
 */
@ControllerAdvice
public class ServerExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServerExceptionHandler.class);

  /**
   * Handle no handler found exception service response.
   *
   * @param ex the ex
   * @return the service response
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleNoHandlerFoundException(Exception ex) {
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
  ResponseEntity<ServerErrorDto> handleHttpMessageNotReadableException(Exception ex) {
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
  ResponseEntity<ServerErrorDto> handleMethodArgumentTypeMismatch(Exception ex) {
    return createServiceResponse(ex, ExceptionType.METHOD_ARGUMENT_TYPE_MISMATCH);
  }

  @ExceptionHandler({UnsatisfiedServletRequestParameterException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleUnsatisfiedRequestParameter(Exception ex) {
    return createServiceResponse(ex, ExceptionType.UNSATISFIED_REQUEST_PARAMETER);
  }

  @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleHttpMediaTypeNotSupported(Exception ex) {
    return createServiceResponse(ex, ExceptionType.MEDIA_TYPE_NOT_SUPPORTED);
  }

  /**
   * Handle access denied exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler({AccessDeniedException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleAccessDeniedException(Exception ex) {
    String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
        .map(GrantedAuthority::getAuthority).findAny().orElse("NO ROLE");
    return role.equals(Role.GUEST.getRoleName()) ? createServiceResponse(ex, ExceptionType.AUTHENTICATION_FAILURE) :
        createServiceResponse(ex, ExceptionType.ACCESS_DENIED_FORBIDDEN);
  }

  /**
   * Handle access denied exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler({BadCredentialsException.class, InternalAuthenticationServiceException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleBadCredentialsException(Exception ex) {
    return createServiceResponse(ex, ExceptionType.AUTHENTICATION_FAILURE);
  }

  @ExceptionHandler({MissingServletRequestParameterException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleMissingServletRequestParameterException(Exception ex) {
    return createServiceResponse(ex, ExceptionType.MISSING_REQUEST_PARAMETER);
  }

  /**
   * Handle server exception service response.
   *
   * @param ex the ex
   * @return the service response
   */
  @ExceptionHandler({ServerException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleServerException(ServerException ex) {
    return createServiceResponse(ex, ex.getExceptionType());
  }

  /**
   * Handle internal server error service response.
   *
   * @param ex the ex
   * @return the service response
   */
  @ExceptionHandler(Exception.class)
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleInternalServerError(Exception ex) {
    return createServiceResponse(ex, ExceptionType.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<ServerErrorDto> createServiceResponse(Exception ex, ExceptionType exceptionType) {
    String exceptionMessage = ex instanceof ServerException ? exceptionType.getMessage() : ex.getMessage();
    LOGGER.error(ex.getClass() + " " + exceptionMessage);
    return ResponseEntity
        .status(HttpStatus.valueOf(exceptionType.getHttpErrorCode()))
        .body(new ServerErrorDto(exceptionType.getServerErrorCode(), exceptionType.getMessage()));
  }
}