package com.epam.esm.dto;

import com.epam.esm.adapter.LocalDateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "token")
@XmlAccessorType(XmlAccessType.FIELD)
public class TokenDto {

  private String token;
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  private LocalDateTime issuedAt;
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  private LocalDateTime expiresAt;

  public TokenDto() {
  }

  public TokenDto(String token, LocalDateTime issuedAt, LocalDateTime expiresAt) {
    this.token = token;
    this.issuedAt = issuedAt;
    this.expiresAt = expiresAt;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
  public LocalDateTime getIssuedAt() {
    return issuedAt;
  }

  public void setIssuedAt(LocalDateTime issuedAt) {
    this.issuedAt = issuedAt;
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(LocalDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }
}