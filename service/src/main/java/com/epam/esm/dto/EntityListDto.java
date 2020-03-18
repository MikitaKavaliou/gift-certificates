package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;


public class EntityListDto<T> {

  @JsonProperty("data")
  private List<T> giftCertificatesWithTags;
  private Integer pagesCount;

  public EntityListDto() {
  }

  public EntityListDto(List<T> giftCertificatesWithTags) {
    this.giftCertificatesWithTags = giftCertificatesWithTags;
  }

  public EntityListDto(List<T> giftCertificatesWithTags, Integer pagesCount) {
    this.giftCertificatesWithTags = giftCertificatesWithTags;
    this.pagesCount = pagesCount;
  }

  public List<T> getGiftCertificatesWithTags() {
    return giftCertificatesWithTags;
  }


  public void setGiftCertificatesWithTags(List<T> giftCertificatesWithTags) {
    this.giftCertificatesWithTags = giftCertificatesWithTags;
  }

  public Integer getPagesCount() {
    return pagesCount;
  }

  public void setPagesCount(Integer pagesCount) {
    this.pagesCount = pagesCount;
  }
}