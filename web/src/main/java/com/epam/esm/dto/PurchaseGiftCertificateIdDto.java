package com.epam.esm.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "purchase")
@XmlAccessorType(XmlAccessType.FIELD)
public class PurchaseGiftCertificateIdDto {

  private Long giftCertificateId;

  public PurchaseGiftCertificateIdDto() {
  }

  public PurchaseGiftCertificateIdDto(Long giftCertificateId) {
    this.giftCertificateId = giftCertificateId;
  }

  public Long getGiftCertificateId() {
    return giftCertificateId;
  }

  public void setGiftCertificateId(Long giftCertificateId) {
    this.giftCertificateId = giftCertificateId;
  }
}