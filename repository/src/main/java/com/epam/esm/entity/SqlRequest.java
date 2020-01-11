package com.epam.esm.entity;

public class SqlRequest {

  private String sqlString;
  private Object[] requestParameters;

  public SqlRequest(String sqlString, Object[] requestParameters) {
    this.sqlString = sqlString;
    this.requestParameters = requestParameters;
  }

  public String getSqlString() {
    return sqlString;
  }

  public Object[] getRequestParameters() {
    return requestParameters;
  }
}