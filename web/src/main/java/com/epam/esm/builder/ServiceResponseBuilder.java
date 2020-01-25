package com.epam.esm.builder;

import com.epam.esm.entity.ServiceResponse;

public class ServiceResponseBuilder<T> {

  private String status;
  private T data;
  private String errorMessage;

  public ServiceResponseBuilder<T> status(String status) {
    this.status = status;
    return this;
  }

  public ServiceResponseBuilder<T> errorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
    return this;
  }

  public ServiceResponseBuilder<T> data(T data) {
    this.data = data;
    return this;
  }

  public ServiceResponse<T> build() {
    return new ServiceResponse<>(status, data, errorMessage);
  }
}