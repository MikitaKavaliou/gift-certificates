package com.epam.esm.repository.specification.impl.tag;

import com.epam.esm.entity.SqlRequest;
import com.epam.esm.repository.specification.Specification;

/**
 * The type Tag name specification. Creates database query for finding tag by name.
 */
public class TagNameSpecification implements Specification {

  private static final String SELECT_BY_NAME_QUERY = "SELECT tag_id, name FROM tag WHERE name = ?";

  private String tagName;

  private TagNameSpecification() {
  }

  /**
   * Instantiates a new Tag name specification.
   *
   * @param tagName the tag name
   */
  public TagNameSpecification(String tagName) {
    this.tagName = tagName;
  }

  @Override
  public SqlRequest createSqlRequest() {
    return new SqlRequest(SELECT_BY_NAME_QUERY, new String[]{tagName});
  }
}