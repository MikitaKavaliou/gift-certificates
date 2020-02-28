package com.epam.esm.dto;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "giftCertificate")
@XmlAccessorType(XmlAccessType.FIELD)
public class GiftCertificateUpdateDto {

  private String name;
  private String description;
  private BigDecimal price;
  private Integer duration;
  @XmlElementWrapper(name = "tagsForAdding")
  @XmlElement(name = "tag")
  private List<Tag> tagsForAdding;
  @XmlElementWrapper(name = "tagsForDeletion")
  @XmlElement(name = "tag")
  private List<Tag> tagsForDeletion;

  public GiftCertificateUpdateDto() {
  }

  public GiftCertificateUpdateDto(String name, String description, BigDecimal price, Integer duration,
      List<Tag> tagsForAdding, List<Tag> tagsForDeletion) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.duration = duration;
    this.tagsForAdding = tagsForAdding;
    this.tagsForDeletion = tagsForDeletion;
  }

  @JsonIgnore
  public GiftCertificate getGiftCertificate() {
    return new GiftCertificate(name, description, price, duration);
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

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public List<Tag> getTagsForAdding() {
    return tagsForAdding;
  }

  public void setTagsForAdding(List<Tag> tagsForAdding) {
    this.tagsForAdding = tagsForAdding;
  }

  public List<Tag> getTagsForDeletion() {
    return tagsForDeletion;
  }

  public void setTagsForDeletion(List<Tag> tagsForDeletion) {
    this.tagsForDeletion = tagsForDeletion;
  }
}