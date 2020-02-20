package com.epam.esm.aspect;

import com.epam.esm.exception.ServerException;
import java.util.Locale;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ConditionalOnProperty(
    value = "exception.logging.enabled",
    havingValue = "true",
    matchIfMissing = true)
public class ExceptionLoggingAspect {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionLoggingAspect.class);
  private final MessageSource messageSource;

  public ExceptionLoggingAspect(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Pointcut("execution(* com.epam.esm.controller.*.* (..))")
  public void exceptionPointcut() {
  }

  @AfterThrowing(pointcut = "exceptionPointcut()", throwing = "exception")
  public void logAllExceptions(JoinPoint joinPoint, Throwable exception) {
    String exceptionMessage = exception.getClass() == ServerException.class ?
        messageSource.getMessage(exception.getMessage(), null, Locale.ENGLISH) : exception.getMessage();
    LOGGER.warn("Exception \"{}\" with message \"{}\" thrown from \"{} {}\"", exception.getClass(), exceptionMessage,
        joinPoint.getSignature().getDeclaringType(),
        joinPoint.getSignature().getName());
  }
}