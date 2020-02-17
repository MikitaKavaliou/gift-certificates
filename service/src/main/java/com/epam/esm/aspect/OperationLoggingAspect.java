package com.epam.esm.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class OperationLoggingAspect {

  private static final Logger LOGGER = LoggerFactory.getLogger(OperationLoggingAspect.class);

  @Pointcut("@within(org.apache.ibatis.annotations.Mapper)")
  public void operationalPointcut() {
  }

  @Around("operationalPointcut()")
  public Object logOperations(ProceedingJoinPoint joinPoint) throws Throwable {
    StopWatch watch = new StopWatch();
    watch.start();
    LOGGER.info("Operation {} {} is started", joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName());
    Object object = joinPoint.proceed();
    watch.stop();
    LOGGER.info("Operation {} {} is ended after {} nanos", joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName(), watch.getTotalTimeNanos());
    return object;
  }
}