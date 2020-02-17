package com.epam.esm.exception;

import com.epam.esm.dto.ServerErrorDto;
import com.epam.esm.model.Role;
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
 * The type Server exception handler.
 */
@ControllerAdvice
public class ServerExceptionHandler {

  /**
   * Handle no handler found exception response entity.
   *
   * @return the response entity
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleNoHandlerFoundException() {
    return createServiceResponse(ExceptionType.NOT_FOUND_HANDLER);
  }

  /**
   * Handle http message not readable exception response entity.
   *
   * @return the response entity
   */
  @ExceptionHandler({HttpMessageNotReadableException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleHttpMessageNotReadableException() {
    return createServiceResponse(ExceptionType.MESSAGE_NOT_READABLE);
  }

  /**
   * Handle method argument type mismatch response entity.
   *
   * @return the response entity
   */
  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleMethodArgumentTypeMismatch() {
    return createServiceResponse(ExceptionType.METHOD_ARGUMENT_TYPE_MISMATCH);
  }

  /**
   * Handle unsatisfied request parameter response entity.
   *
   * @return the response entity
   */
  @ExceptionHandler({UnsatisfiedServletRequestParameterException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleUnsatisfiedRequestParameter() {
    return createServiceResponse(ExceptionType.UNSATISFIED_REQUEST_PARAMETER);
  }

  /**
   * Handle http media type not supported response entity.
   *
   * @return the response entity
   */
  @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleHttpMediaTypeNotSupported() {
    return createServiceResponse(ExceptionType.MEDIA_TYPE_NOT_SUPPORTED);
  }

  /**
   * Handle access denied exception response entity.
   *
   * @return the response entity
   */
  @ExceptionHandler({AccessDeniedException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleAccessDeniedException() {
    String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
        .map(GrantedAuthority::getAuthority).findAny().orElse("NO ROLE");
    return role.equals(Role.GUEST.getRoleName()) ? createServiceResponse(ExceptionType.AUTHENTICATION_FAILURE) :
        createServiceResponse(ExceptionType.ACCESS_DENIED_FORBIDDEN);
  }

  /**
   * Handle bad credentials exception response entity.
   *
   * @return the response entity
   */
  @ExceptionHandler({BadCredentialsException.class, InternalAuthenticationServiceException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleBadCredentialsException() {
    return createServiceResponse(ExceptionType.AUTHENTICATION_FAILURE);
  }

  /**
   * Handle missing servlet request parameter exception response entity.
   *
   * @return the response entity
   */
  @ExceptionHandler({MissingServletRequestParameterException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleMissingServletRequestParameterException() {
    return createServiceResponse(ExceptionType.MISSING_REQUEST_PARAMETER);
  }

  /**
   * Handle server exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler({ServerException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleServerException(ServerException ex) {
    return createServiceResponse(ex.getExceptionType());
  }

  /**
   * Handle internal server error response entity.
   *
   * @return the response entity
   */
  @ExceptionHandler(Exception.class)
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleInternalServerError() {
    return createServiceResponse(ExceptionType.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<ServerErrorDto> createServiceResponse(ExceptionType exceptionType) {
    return ResponseEntity
        .status(HttpStatus.valueOf(exceptionType.getHttpErrorCode()))
        .body(new ServerErrorDto(exceptionType.getServerErrorCode(), exceptionType.getMessage()));
  }
}