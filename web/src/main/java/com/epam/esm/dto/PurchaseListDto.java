package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class PurchaseListDto {

  @XmlElement(name = "purchase")
  @JsonProperty("data")
  private List<PurchaseWithCertificateDto> purchases;

  public PurchaseListDto() {
  }

  public PurchaseListDto(List<PurchaseWithCertificateDto> purchases) {
    this.purchases = purchases;
  }

  public List<PurchaseWithCertificateDto> getPurchases() {
    return purchases;
  }

  public void setPurchases(List<PurchaseWithCertificateDto> purchases) {
    this.purchases = purchases;
  }
}