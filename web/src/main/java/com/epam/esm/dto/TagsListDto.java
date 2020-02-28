package com.epam.esm.dto;

import com.epam.esm.model.Tag;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class TagsListDto {

  @XmlElement(name = "tag")
  @JsonProperty("data")
  private List<Tag> tags;

  public TagsListDto() {
  }

  public TagsListDto(List<Tag> tags) {
    this.tags = tags;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }
}