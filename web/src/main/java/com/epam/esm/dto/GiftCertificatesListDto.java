package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class GiftCertificatesListDto {

  @XmlElement(name = "giftCertificate")
  @JsonProperty("data")
  private List<GiftCertificateWithTagsDto> giftCertificatesWithTags;

  public GiftCertificatesListDto() {
  }

  public GiftCertificatesListDto(
      List<GiftCertificateWithTagsDto> giftCertificatesWithTags) {
    this.giftCertificatesWithTags = giftCertificatesWithTags;
  }

  public List<GiftCertificateWithTagsDto> getGiftCertificatesWithTags() {
    return giftCertificatesWithTags;
  }


  public void setGiftCertificatesWithTags(List<GiftCertificateWithTagsDto> giftCertificatesWithTags) {
    this.giftCertificatesWithTags = giftCertificatesWithTags;
  }
}