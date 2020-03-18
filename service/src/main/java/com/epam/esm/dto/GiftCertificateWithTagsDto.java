package com.epam.esm.dto;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class GiftCertificateWithTagsDto {

  private Long id;
  private String name;
  private String description;
  private BigDecimal price;
  private LocalDateTime createDate;
  private LocalDateTime lastUpdateDate;
  private Integer duration;
  private List<Tag> tags;

  public GiftCertificateWithTagsDto() {
  }

  public GiftCertificateWithTagsDto(GiftCertificate giftCertificate, List<Tag> tags) {
    this.id = giftCertificate.getId();
    this.name = giftCertificate.getName();
    this.description = giftCertificate.getDescription();
    this.price = giftCertificate.getPrice();
    this.createDate = giftCertificate.getCreateDate();
    this.lastUpdateDate = giftCertificate.getLastUpdateDate();
    this.duration = giftCertificate.getDuration();
    this.tags = tags;
  }

  @JsonIgnore
  public GiftCertificate getGiftCertificate() {
    return new GiftCertificate(id, name, description, price, createDate, lastUpdateDate, duration);
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

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  public LocalDateTime getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
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

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }
}