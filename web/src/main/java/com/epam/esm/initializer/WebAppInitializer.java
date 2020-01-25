package com.epam.esm.initializer;

import javax.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebAppInitializer implements WebApplicationInitializer {

  @Override
  public void onStartup(javax.servlet.ServletContext servletContext) {
    AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
    applicationContext.scan("com.epam.esm");
    servletContext.addListener(new ContextLoaderListener(applicationContext));
    DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);
    dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
    ServletRegistration.Dynamic appServlet =
        servletContext.addServlet("dispatcher", dispatcherServlet);
    appServlet.setLoadOnStartup(1);
    appServlet.addMapping("/");
  }
}