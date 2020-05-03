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
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class FileProcessor implements Runnable {

  private final LinkedBlockingQueue<File> filesQueue;
  private final GiftCertificateMapper certificateMapper;
  private final FileMapper fileMapper;
  private final File errorFolder;
  private static final ReentrantLock LOCK = new ReentrantLock();

  private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessor.class);

  public FileProcessor(LinkedBlockingQueue<File> filesQueue, GiftCertificateMapper certificateMapper,
      FileMapper fileMapper, File errorFolder) {
    this.filesQueue = filesQueue;
    this.certificateMapper = certificateMapper;
    this.errorFolder = errorFolder;
    this.fileMapper = fileMapper;
  }

  @Override
  public void run() {
    File file = null;
    try {
      while ((file = filesQueue.poll(2, TimeUnit.SECONDS)) != null) {
        processFile(file);
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
        if (file.exists()) {
          tryToReadFile(file);
        }
      } catch (JsonParseException | UnrecognizedPropertyException | InvalidFormatException | DataIntegrityViolationException e) {
        moveFileToErrorFolder(file);
      } finally {
        fileMapper.deleteByPath(file.getAbsolutePath());
      }
    }
  }

  private void tryToReadFile(File file) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    List<GiftCertificate> certificates = Arrays.asList(objectMapper.readValue(file, GiftCertificate[].class));
    certificateMapper.insertList(certificates);
    file.delete();
  }

  private void moveFileToErrorFolder(File file) throws IOException {
    try {
      LOCK.lock();
      Files.move(Paths.get(file.getAbsolutePath()), getPathForMovingFile(file), StandardCopyOption.ATOMIC_MOVE);
    } finally {
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