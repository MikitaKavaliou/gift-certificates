package com.epam.esm;

import de.invesdwin.instrument.DynamicInstrumentationLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableLoadTimeWeaving;

@SpringBootApplication
@EnableLoadTimeWeaving
public class ApplicationStarter {

  public static void main(String[] args) {
    DynamicInstrumentationLoader.waitForInitialized();
    DynamicInstrumentationLoader.initLoadTimeWeavingContext();
    SpringApplication.run(ApplicationStarter.class, args);
  }
}