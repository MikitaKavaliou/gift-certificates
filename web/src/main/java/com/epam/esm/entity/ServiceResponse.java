package com.epam.esm.entity;


public class ServiceResponse<T> {

  private String status;
  private T data;
  private String errorMessage;

  public ServiceResponse(String status, T data, String errorMessage) {
    this.status = status;
    this.data = data;
    this.errorMessage = errorMessage;
  }

  public ServiceResponse(String status, String errorMessage, T data) {
    this.status = status;
    this.errorMessage = errorMessage;
    this.data = data;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}