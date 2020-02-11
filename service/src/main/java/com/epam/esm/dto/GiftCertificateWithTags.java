package com.epam.esm.dto;

import com.epam.esm.adapter.ZonedDateTimeAdapter;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlRootElement(name = "certificate")
@XmlAccessorType(XmlAccessType.FIELD)
public class GiftCertificateWithTags {

  private Long id;
  private String name;
  private String description;
  private BigDecimal price;
  @XmlElement
  @XmlJavaTypeAdapter(ZonedDateTimeAdapter.class)
  @JsonProperty(access = Access.READ_ONLY)
  private ZonedDateTime createDate;
  @XmlElement
  @XmlJavaTypeAdapter(ZonedDateTimeAdapter.class)
  @JsonProperty(access = Access.READ_ONLY)
  private ZonedDateTime lastUpdateDate;
  private Integer duration;
  @XmlElementWrapper(name = "tags")
  @XmlElement(name = "tag")
  private List<Tag> tags;
  @XmlElementWrapper(name = "tagsForDeletion")
  @XmlElement(name = "tagForDeletion")
  private List<Tag> tagsForDeletion;

  public GiftCertificateWithTags() {
  }

  public GiftCertificateWithTags(GiftCertificate giftCertificate, List<Tag> tags) {
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

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  public ZonedDateTime getCreateDate() {
    return createDate;
  }

  public void setCreateDate(ZonedDateTime createDate) {
    this.createDate = createDate;
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  public ZonedDateTime getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(ZonedDateTime lastUpdateDate) {
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

  public List<Tag> getTagsForDeletion() {
    return tagsForDeletion;
  }

  public void setTagsForDeletion(List<Tag> tagsForDeletion) {
    this.tagsForDeletion = tagsForDeletion;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, price, createDate, lastUpdateDate, duration, tags);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GiftCertificateWithTags that = (GiftCertificateWithTags) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(name, that.name) &&
        Objects.equals(description, that.description) &&
        Objects.equals(price, that.price) &&
        Objects.equals(createDate, that.createDate) &&
        Objects.equals(lastUpdateDate, that.lastUpdateDate) &&
        Objects.equals(duration, that.duration) &&
        Objects.equals(tags, that.tags);
  }
}