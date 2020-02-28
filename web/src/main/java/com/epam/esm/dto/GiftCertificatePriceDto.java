package com.epam.esm.dto;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "giftCertificate")
@XmlAccessorType(XmlAccessType.FIELD)
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