package com.epam.esm.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Purchase {

  private Long id;
  private Long userId;
  private Long giftCertificateId;
  private BigDecimal cost;

  public Purchase() {
  }

  public Purchase(Long id, Long userId, Long giftCertificateId, BigDecimal cost) {
    this.id = id;
    this.userId = userId;
    this.giftCertificateId = giftCertificateId;
    this.cost = cost;
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