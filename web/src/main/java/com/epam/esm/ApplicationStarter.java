package com.epam.esm;

import de.invesdwin.instrument.DynamicInstrumentationLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableLoadTimeWeaving;

@SpringBootApplication
@EnableLoadTimeWeaving
public class ApplicationStarter {

  public static void main(String[] args) {
    DynamicInstrumentationLoader.waitForInitialized(); //dynamically attach java agent to jvm if not already present
    DynamicInstrumentationLoader.initLoadTimeWeavingContext(); //weave all classes before they are loaded as beans
    SpringApplication.run(ApplicationStarter.class, args);
  }
}