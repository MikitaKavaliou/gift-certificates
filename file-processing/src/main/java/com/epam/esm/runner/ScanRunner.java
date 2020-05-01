package com.epam.esm.runner;

import com.epam.esm.processor.FolderProcessor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ScanRunner {

  @Value("${file-scan-delay}")
  private final double scanDelay;
  private final FolderProcessor folderProcessor;

  public ScanRunner(@Value("${file-scan-delay}") double scanDelay, FolderProcessor folderProcessor) {
    this.scanDelay = scanDelay;
    this.folderProcessor = folderProcessor;
  }

  @EventListener
  public void startScheduledScanRunner(ContextRefreshedEvent event) {
    long convertedScanDelay = (long) (scanDelay * 1000);
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    scheduledExecutorService.scheduleWithFixedDelay(folderProcessor, 0, convertedScanDelay, TimeUnit.MILLISECONDS);
  }
}