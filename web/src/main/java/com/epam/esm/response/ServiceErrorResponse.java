package com.epam.esm.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ServiceErrorResponse {

  private int errorCode;
  private String errorMessage;

  private ServiceErrorResponse() {
  }

  public ServiceErrorResponse(int errorCode, String errorMessage) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}