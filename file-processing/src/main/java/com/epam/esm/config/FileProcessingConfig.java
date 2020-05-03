package com.epam.esm.config;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileProcessingConfig {

  @Value("${processing-threads-count}")
  private int threadCount;
  @Value("${folder-path}")
  private String rootFolderPath;
  @Value("${error-folder-name}")
  private String errorFolderName;

  @Bean
  public LinkedBlockingQueue<File> filesQueue() {
    return new LinkedBlockingQueue<>(threadCount * 2);
  }

  @Bean
  public File errorFolder() {
    return new File(rootFolderPath, errorFolderName);
  }
}