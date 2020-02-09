package com.epam.esm.interceptor;

import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.validation.ContentTypeValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ContentTypeInterceptor extends HandlerInterceptorAdapter {

  private static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    String contentType = request.getHeader(CONTENT_TYPE_HEADER_NAME);
    if (!ContentTypeValidator.isValidContentType(contentType)) {
      throw new ServerException(ExceptionType.CONTENT_TYPE_NOT_SUPPORTED);
    }
    response.setHeader(CONTENT_TYPE_HEADER_NAME, contentType);
    return true;
  }
}