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
public class TransactionLoggingAspect {

  private static final Logger LOGGER = LoggerFactory.getLogger(TransactionLoggingAspect.class);

  @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
  public void transactionalPointcut() {
  }

  @Around("transactionalPointcut()")
  public Object logTransactions(ProceedingJoinPoint joinPoint) throws Throwable {
    StopWatch watch = new StopWatch();
    watch.start();
    LOGGER.info("Transaction {} {} is started", joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName());
    Object object = joinPoint.proceed();
    watch.stop();
    LOGGER.info("Transaction {} {} is ended after {} milliseconds", joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName(), watch.getTotalTimeMillis());
    return object;
  }
}