package com.epam.esm.aspect;

import java.io.File;
import java.io.IOException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FileProcessingLoggingAspect {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessingLoggingAspect.class);

  @Pointcut("execution(private void com.epam.esm.processor.FileProcessor.processFile(java.io.File))")
  public void processFilePointcut() {
  }

  @Around("processFilePointcut()")
  public Object logFileProcessing(ProceedingJoinPoint joinPoint) throws Throwable {
    File file = (File) joinPoint.getArgs()[0];
    long time = System.currentTimeMillis();
    try {
      Object object = joinPoint.proceed();
      long resultTime = System.currentTimeMillis() - time;
      LOGGER.info("Processing of file {} took {} milliseconds and completed correctly", file.getAbsolutePath(),
          resultTime);
      return object;
    } catch (IOException e) {
      long resultTime = System.currentTimeMillis() - time;
      LOGGER.error("Processing of file {} took {} milliseconds and completed with an error", file.getAbsolutePath(),
          resultTime);
      throw e;
    }
  }
}