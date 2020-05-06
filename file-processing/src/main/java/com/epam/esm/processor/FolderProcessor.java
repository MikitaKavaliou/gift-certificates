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
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

@Component
public class FolderProcessor implements Runnable {

  private static final Logger LOGGER = LoggerFactory.getLogger(FolderProcessor.class);
  private final int threadCount;
  private final String rootFolderPath;
  private final File errorFolder;
  private final LinkedBlockingQueue<File> files;
  private final ApplicationContext context;
  private final FileMapper fileMapper;
  private final AtomicBoolean isScanEnded;

  public FolderProcessor(
      @Value("${processing-threads-count}") int threadCount,
      @Value("${folder-path}") String rootFolderPath,
      File errorFolder,
      LinkedBlockingQueue<File> files,
      ApplicationContext context,
      FileMapper fileMapper,
      AtomicBoolean isScanEnded) {
    this.threadCount = threadCount;
    this.rootFolderPath = rootFolderPath;
    this.errorFolder = errorFolder;
    this.files = files;
    this.context = context;
    this.fileMapper = fileMapper;
    this.isScanEnded = isScanEnded;
  }

  @Override
  public void run() {
    File rootFolder = new File(rootFolderPath);
    try {
      createErrorFolder();
      isScanEnded.set(false);
      loadFiles(rootFolder, true);
      if (!files.isEmpty()) {
        ExecutorService executor = runThreadPool();
        loadFiles(rootFolder, false);
        isScanEnded.set(true);
        waitForFileProcessing(executor);
      }
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
          if (file.exists()) {
            this.files.put(file);
          } else {
            fileMapper.deleteByPath(file.getAbsolutePath());
          }
        } catch (DuplicateKeyException e) {
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