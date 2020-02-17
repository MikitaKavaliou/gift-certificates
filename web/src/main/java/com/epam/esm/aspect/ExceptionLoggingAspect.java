package com.epam.esm.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionLoggingAspect {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionLoggingAspect.class);

  @Pointcut("execution(* com.epam.esm.controller.*.* (..))")
  public void exceptionPointcut() {
  }

  @AfterThrowing(pointcut = "exceptionPointcut()", throwing = "exception")
  public void logAllExceptions(JoinPoint joinPoint, Throwable exception) {
    LOGGER.warn("Exception {} with message {} thrown from {} {}", exception.getClass(), exception.getMessage(),
        joinPoint.getSignature().getDeclaringType(),
        joinPoint.getSignature().getName());
  }
}
