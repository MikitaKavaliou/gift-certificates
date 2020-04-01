package com.epam.esm.dto;

import java.math.BigDecimal;


public class GiftCertificatePriceDto {

  private BigDecimal price;

  public GiftCertificatePriceDto() {
  }

  public GiftCertificatePriceDto(BigDecimal price) {
    this.price = price;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }
}