package com.epam.esm.repository.specification.impl.tag;

import com.epam.esm.entity.SqlRequest;
import com.epam.esm.repository.specification.Specification;

/**
 * The type Tag id specification. Creates database query for finding tag by id.
 */
public class TagIdSpecification implements Specification {

  private static final String SELECT_BY_ID_QUERY = "SELECT tag_id, name FROM tag WHERE tag_id = ?";
  private Long id;

  private TagIdSpecification() {
  }

  /**
   * Instantiates a new Tag id specification.
   *
   * @param id the id
   */
  public TagIdSpecification(Long id) {
    this.id = id;
  }

  @Override
  public SqlRequest createSqlRequest() {
    return new SqlRequest(SELECT_BY_ID_QUERY, new Long[]{id});
  }
}