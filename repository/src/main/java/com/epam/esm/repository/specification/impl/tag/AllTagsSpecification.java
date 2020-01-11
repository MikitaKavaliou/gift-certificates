package com.epam.esm.repository.specification.impl.tag;

import com.epam.esm.entity.SqlRequest;
import com.epam.esm.repository.specification.Specification;

/**
 * The type All tags specification. Creates database query for finding all tags.
 */
public class AllTagsSpecification implements Specification {

  private static final String SELECT_ALL_QUERY = "SELECT tag_id, name FROM tag";

  @Override
  public SqlRequest createSqlRequest() {
    return new SqlRequest(SELECT_ALL_QUERY, new Object[]{});
  }
}