package com.epam.esm.dto;

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