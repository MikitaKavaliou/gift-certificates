package com.epam.esm.processor;

import com.epam.esm.mapper.FileMapper;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.model.GiftCertificate;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class FileProcessor implements Runnable {

  private static final ReentrantLock LOCK = new ReentrantLock();
  private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessor.class);
  private final LinkedBlockingQueue<File> filesQueue;
  private final GiftCertificateMapper certificateMapper;
  private final FileMapper fileMapper;
  private final File errorFolder;
  private final AtomicBoolean hasFolderProcessorFinishedScanning;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public FileProcessor(LinkedBlockingQueue<File> filesQueue, GiftCertificateMapper certificateMapper,
      FileMapper fileMapper, File errorFolder, AtomicBoolean hasFolderProcessorFinishedScanning) {
    this.filesQueue = filesQueue;
    this.certificateMapper = certificateMapper;
    this.fileMapper = fileMapper;
    this.errorFolder = errorFolder;
    this.hasFolderProcessorFinishedScanning = hasFolderProcessorFinishedScanning;
  }

  @Override
  public void run() {
    File file = null;
    try {
      while (!hasFolderProcessorFinishedScanning.get() || !filesQueue.isEmpty()) {
        file = filesQueue.poll(100, TimeUnit.MILLISECONDS);
        if (file != null) {
          processFile(file);
        }
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      LOGGER.warn("File thread was interrupted");
    } catch (IOException e) {
      LOGGER.error("Error processing file {}", file.getAbsolutePath(), e);
    }
  }

  private void processFile(File file) throws IOException {
    if (file != null) {
      try {
        tryToReadFile(file);
      } catch (JsonParseException | UnrecognizedPropertyException | InvalidFormatException | DataIntegrityViolationException e) {
        moveFileToErrorFolder(file);
      } finally {
        fileMapper.deleteByPath(file.getAbsolutePath());
      }
    }
  }

  private void tryToReadFile(File file) throws IOException {
    List<GiftCertificate> certificates = Arrays.asList(objectMapper.readValue(file, GiftCertificate[].class));
    certificateMapper.insertList(certificates);
    file.delete();
  }

  private void moveFileToErrorFolder(File file) throws IOException {
    try {
      LOCK.lock();
      boolean isMoved;
      do {
        try {
          fileMapper.insert(errorFolder.getAbsolutePath());
          isMoved = true;
        } catch (DuplicateKeyException | DeadlockLoserDataAccessException e) {
          isMoved = false;
        }
      } while (!isMoved);
      Files.move(Paths.get(file.getAbsolutePath()), getPathForMovingFile(file), StandardCopyOption.ATOMIC_MOVE);
    } finally {
      fileMapper.deleteByPath(errorFolder.getAbsolutePath());
      LOCK.unlock();
    }
  }

  private Path getPathForMovingFile(File fileForMove) {
    File fileList[] = errorFolder.listFiles();
    int filesWithSameNameCounter = 0;
    if (fileList != null && fileList.length > 0) {
      for (File file : fileList) {
        if (file.getName().contains(fileForMove.getName())) {
          filesWithSameNameCounter++;
        }
      }
    }
    return filesWithSameNameCounter > 0 ?
        Paths.get(errorFolder.getAbsolutePath(), "(" + filesWithSameNameCounter + ")" + fileForMove.getName()) :
        Paths.get(errorFolder.getAbsolutePath(), fileForMove.getName());
  }
}