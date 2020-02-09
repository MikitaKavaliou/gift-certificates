package com.epam.esm.validation;

public class ContentTypeValidator {

  private static final String CONTENT_TYPE_APPLICATION_XML = "application/xml";
  private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

  private ContentTypeValidator() {

  }

  public static boolean isValidContentType(String contentType) {
    return contentType == null || contentType.equals(CONTENT_TYPE_APPLICATION_JSON) || contentType
        .equals(CONTENT_TYPE_APPLICATION_XML);
  }
}