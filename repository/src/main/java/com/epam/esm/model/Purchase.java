package com.epam.esm.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Purchase {

  private Long id;
  private Long userId;
  private BigDecimal cost;
  private LocalDateTime purchaseDate;
  private Long giftCertificateId;

  public Purchase() {
  }

  public Purchase(Long id, Long userId, BigDecimal cost, LocalDateTime purchaseDate, Long giftCertificateId) {
    this.id = id;
    this.userId = userId;
    this.cost = cost;
    this.purchaseDate = purchaseDate;
    this.giftCertificateId = giftCertificateId;
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

  public Long getGiftCertificateId() {
    return giftCertificateId;
  }

  public void setGiftCertificateId(Long giftCertificateId) {
    this.giftCertificateId = giftCertificateId;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  public LocalDateTime getPurchaseDate() {
    return purchaseDate;
  }

  public void setPurchaseDate(LocalDateTime purchaseDate) {
    this.purchaseDate = purchaseDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Purchase purchase = (Purchase) o;
    return Objects.equals(id, purchase.id) &&
        Objects.equals(userId, purchase.userId) &&
        Objects.equals(giftCertificateId, purchase.giftCertificateId) &&
        Objects.equals(cost, purchase.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, giftCertificateId, cost);
  }

  @Override
  public String toString() {
    return "Purchase{" +
        "id=" + id +
        ", userId=" + userId +
        ", giftCertificateId=" + giftCertificateId +
        ", cost=" + cost +
        '}';
  }
}