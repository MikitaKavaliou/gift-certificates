package com.epam.esm.entity;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "certificate")
@XmlAccessorType(XmlAccessType.FIELD)
public class CertificateWithTags {

  private Long id;
  private String name;
  private String description;
  private BigDecimal price;
  private ZonedDateTime createDate;
  private ZonedDateTime lastUpdateDate;
  private Integer duration;
  @XmlElementWrapper(name = "tags")
  @XmlElement(name = "tag")
  private List<Tag> tags;

  public CertificateWithTags() {
  }

  public CertificateWithTags(GiftCertificate giftCertificate, List<Tag> tags) {
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

  public List<Tag> getTags() {
    return tags;
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

  public String getDescription() {
    return description;
  }

  public BigDecimal getPrice() {
    return price;
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  public ZonedDateTime getCreateDate() {
    return createDate;
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  public ZonedDateTime getLastUpdateDate() {
    return lastUpdateDate;
  }

  public Integer getDuration() {
    return duration;
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
    CertificateWithTags that = (CertificateWithTags) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(name, that.name) &&
        Objects.equals(description, that.description) &&
        Objects.equals(price, that.price) &&
        Objects.equals(createDate, that.createDate) &&
        Objects.equals(lastUpdateDate, that.lastUpdateDate) &&
        Objects.equals(duration, that.duration) &&
        Objects.equals(tags, that.tags);
  }

  @Override
  public String toString() {
    return "CertificateWithTags{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        ", price=" + price +
        ", createDate=" + createDate +
        ", lastUpdateDate=" + lastUpdateDate +
        ", duration=" + duration +
        ", tags=" + tags +
        '}';
  }
}