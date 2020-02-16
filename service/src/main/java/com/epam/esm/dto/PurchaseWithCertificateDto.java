package com.epam.esm.dto;

import com.epam.esm.model.Purchase;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PurchaseWithCertificateDto {

  private Long id;
  private Long userId;
  private BigDecimal cost;
  private LocalDateTime purchaseDate;
  private String certificateUrl;

  public PurchaseWithCertificateDto() {
  }

  public PurchaseWithCertificateDto(Purchase purchase, String certificateUrl) {
    this.id = purchase.getId();
    this.userId = purchase.getUserId();
    this.cost = purchase.getCost();
    this.purchaseDate = purchase.getPurchaseDate();
    this.certificateUrl = certificateUrl;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  public String getCertificateUrl() {
    return certificateUrl;
  }

  public void setCertificateUrl(String certificateUrl) {
    this.certificateUrl = certificateUrl;
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
  public LocalDateTime getPurchaseDate() {
    return purchaseDate;
  }

  public void setPurchaseDate(LocalDateTime purchaseDate) {
    this.purchaseDate = purchaseDate;
  }
}