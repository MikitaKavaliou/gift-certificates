package com.epam.esm.processor;

import com.epam.esm.mapper.FileMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class FolderProcessor implements Runnable {

  private final int threadCount;
  private final String rootFolderPath;
  private final File errorFolder;
  private final LinkedBlockingQueue<File> files;
  private final ApplicationContext context;
  private final FileMapper fileMapper;

  private static final Logger LOGGER = LoggerFactory.getLogger(FolderProcessor.class);

  public FolderProcessor(
      @Value("${processing-threads-count}") int threadCount,
      @Value("${folder-path}") String rootFolderPath,
      File errorFolder,
      LinkedBlockingQueue<File> files,
      ApplicationContext context,
      FileMapper fileMapper) {
    this.threadCount = threadCount;
    this.rootFolderPath = rootFolderPath;
    this.errorFolder = errorFolder;
    this.files = files;
    this.context = context;
    this.fileMapper = fileMapper;
  }

  @Override
  public void run() {
    File rootFolder = new File(rootFolderPath);
    try {
      createErrorFolder();
      loadFiles(rootFolder, true);
      ExecutorService executor = runThreadPool();
      loadFiles(rootFolder, false);
      waitForFileProcessing(executor);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      LOGGER.warn("Scan thread was interrupted");
    }
  }


  private void createErrorFolder() {
    if (!errorFolder.exists()) {
      try {
        Files.createDirectory(Paths.get(errorFolder.getAbsolutePath()));
      } catch (IOException e) {
        LOGGER.error("error creating error folder");
      }
    }
  }

  private void loadFiles(File folder, boolean isInitialLoad) throws InterruptedException {
    File[] files = folder.listFiles();
    for (File file : files) {
      if (this.files.size() == threadCount * 2 && isInitialLoad) {
        return;
      }
      if (!file.isDirectory()) {
        try {
          fileMapper.insert(file.getAbsolutePath());
          this.files.put(file);
          LOGGER.info("File {} added to queue", file.getAbsolutePath());
        } catch (DataIntegrityViolationException e) {
          LOGGER.info("File {} already in processing", file.getAbsolutePath());
        }
      } else if (file.isDirectory() && !file.getName().equals(errorFolder.getName())) {
        loadFiles(file, isInitialLoad);
      }
    }
  }

  private ExecutorService runThreadPool() {
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    for (int i = 0; i < threadCount; i++) {
      executor.execute(context.getBean(FileProcessor.class));
    }
    return executor;
  }

  private void waitForFileProcessing(ExecutorService executor) throws InterruptedException {
    executor.shutdown();
    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
  }
}