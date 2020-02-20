package com.epam.esm.exception;

import com.epam.esm.dto.ServerErrorDto;
import com.epam.esm.model.Role;
import com.epam.esm.security.SecurityUserDetails;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * The type Server exception handler.
 */
@ControllerAdvice
public class ServerExceptionHandler {

  private final MessageSource messageSource;
  private final LocaleResolver localeResolver;

  /**
   * Instantiates a new Server exception handler.
   *
   * @param messageSource  the message source
   * @param localeResolver the locale resolver
   */
  public ServerExceptionHandler(MessageSource messageSource,
      LocaleResolver localeResolver) {
    this.messageSource = messageSource;
    this.localeResolver = localeResolver;
  }

  /**
   * Handle no handler found exception response entity.
   *
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleNoHandlerFoundException(HttpServletRequest request) {
    return createServiceResponse(ExceptionType.NOT_FOUND_HANDLER, request);
  }

  /**
   * Handle http message not readable exception response entity.
   *
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler({HttpMessageNotReadableException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleHttpMessageNotReadableException(HttpServletRequest request) {
    return createServiceResponse(ExceptionType.MESSAGE_NOT_READABLE, request);
  }

  /**
   * Handle method argument type mismatch response entity.
   *
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleMethodArgumentTypeMismatchException(HttpServletRequest request) {
    return createServiceResponse(ExceptionType.METHOD_ARGUMENT_TYPE_MISMATCH, request);
  }

  /**
   * Handle unsatisfied request parameter response entity.
   *
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler({UnsatisfiedServletRequestParameterException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleUnsatisfiedRequestParameterException(HttpServletRequest request) {
    return createServiceResponse(ExceptionType.UNSATISFIED_REQUEST_PARAMETER, request);
  }

  /**
   * Handle http media type not supported response entity.
   *
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleHttpMediaTypeNotSupportedException(HttpServletRequest request) {
    return createServiceResponse(ExceptionType.MEDIA_TYPE_NOT_SUPPORTED, request);
  }


  /**
   * Handle http media type not acceptable exception response entity.
   *
   * @param response the response
   * @param request  the request
   * @return the response entity
   */
  @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleHttpMediaTypeNotAcceptableException(HttpServletResponse response,
      HttpServletRequest request) {
    response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    return createServiceResponse(ExceptionType.MEDIA_TYPE_NOT_ACCEPTABLE, request);
  }

  /**
   * Handle access denied exception response entity.
   *
   * @param userDetails the user details
   * @param request     the request
   * @return the response entity
   */
  @ExceptionHandler({AccessDeniedException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleAccessDeniedException(@AuthenticationPrincipal SecurityUserDetails userDetails
      , HttpServletRequest request) {
    return userDetails.getRole().getRoleName().equals(Role.GUEST.getRoleName()) ?
        createServiceResponse(ExceptionType.AUTHENTICATION_FAILURE, request) :
        createServiceResponse(ExceptionType.ACCESS_DENIED, request);
  }

  /**
   * Handle bad credentials exception response entity.
   *
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler({BadCredentialsException.class, InternalAuthenticationServiceException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleBadCredentialsException(HttpServletRequest request) {
    return createServiceResponse(ExceptionType.AUTHENTICATION_FAILURE, request);
  }

  /**
   * Handle missing servlet request parameter exception response entity.
   *
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleHttpRequestMethodNotSupportedException(HttpServletRequest request) {
    return createServiceResponse(ExceptionType.HTTP_REQUEST_METHOD_NOT_SUPPORTED, request);
  }

  /**
   * Handle missing servlet request parameter exception response entity.
   *
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler({MissingServletRequestParameterException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleMissingServletRequestParameterException(HttpServletRequest request) {
    return createServiceResponse(ExceptionType.MISSING_REQUEST_PARAMETER, request);
  }

  /**
   * Handle server exception response entity.
   *
   * @param ex      the ex
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler({ServerException.class})
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleServerException(ServerException ex, HttpServletRequest request) {
    return createServiceResponse(ex.getExceptionType(), request);
  }

  /**
   * Handle internal server error response entity.
   *
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler(Exception.class)
  public @ResponseBody
  ResponseEntity<ServerErrorDto> handleInternalServerError(HttpServletRequest request) {
    return createServiceResponse(ExceptionType.INTERNAL_SERVER_ERROR, request);
  }

  private ResponseEntity<ServerErrorDto> createServiceResponse(ExceptionType exceptionType,
      HttpServletRequest request) {
    return ResponseEntity
        .status(HttpStatus.valueOf(exceptionType.getHttpErrorCode()))
        .body(new ServerErrorDto(exceptionType.getServerErrorCode(),
            messageSource.getMessage(exceptionType.getMessage(), null, localeResolver.resolveLocale(request))));
  }
}