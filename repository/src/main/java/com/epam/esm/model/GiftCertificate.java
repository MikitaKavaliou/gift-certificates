package com.epam.esm.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


public class GiftCertificate {

  private Long id;
  private String name;
  private String description;
  private BigDecimal price;
  private LocalDateTime createDate;
  private LocalDateTime lastUpdateDate;
  private Integer duration;

  public GiftCertificate() {
  }

  public GiftCertificate(String name, String description, BigDecimal price, Integer duration) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.duration = duration;
  }

  public GiftCertificate(Long id, String name, String description, BigDecimal price, LocalDateTime createDate,
      LocalDateTime lastUpdateDate, Integer duration) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.createDate = createDate;
    this.lastUpdateDate = lastUpdateDate;
    this.duration = duration;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public LocalDateTime getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }

  public LocalDateTime getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GiftCertificate that = (GiftCertificate) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(name, that.name) &&
        Objects.equals(description, that.description) &&
        Objects.equals(price, that.price) &&
        Objects.equals(createDate, that.createDate) &&
        Objects.equals(lastUpdateDate, that.lastUpdateDate) &&
        Objects.equals(duration, that.duration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, price, createDate, lastUpdateDate, duration);
  }

  @Override
  public String toString() {
    return "GiftCertificate{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        ", price=" + price +
        ", createDate=" + createDate +
        ", lastUpdateDate=" + lastUpdateDate +
        ", duration=" + duration +
        '}';
  }
}