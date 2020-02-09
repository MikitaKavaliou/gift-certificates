package com.epam.esm.entity;

import com.epam.esm.model.Tag;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tags")
@XmlAccessorType(XmlAccessType.FIELD)
public class TagList {

  @XmlElement(name = "tag")
  private List<Tag> tags;

  public TagList() {
  }

  public TagList(List<Tag> tags) {
    this.tags = tags;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }
}