package com.epam.esm.repository.impl;

import com.epam.esm.entity.SqlRequest;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.specification.Specification;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Implementation of TagRepository.
 */
@Repository
public class TagRepositoryImpl implements TagRepository {

  private static final String CREATE_TAG_QUERY = "INSERT INTO tag (name) VALUES (?)";
  private static final String DELETE_TAG_QUERY = "DELETE FROM tag WHERE tag_id = ?";
  private static final String ID_COLUMN_NAME = "tag_id";
  private static final String NAME_COLUMN_NAME = "name";

  private final JdbcTemplate jdbcTemplate;

  /**
   * Instantiates a new Tag repository.
   *
   * @param jdbcTemplate the jdbc template is used for execution queries.
   */
  @Autowired
  public TagRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @SuppressWarnings("all")
  @Override
  public Long create(Tag tag) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection
          .prepareStatement(CREATE_TAG_QUERY, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, tag.getName());
      return ps;
    }, keyHolder);

    return keyHolder.getKeys().size() > 1 ?
        Long.valueOf((Integer) keyHolder.getKeys().get("tag_id")) : keyHolder.getKey().longValue();
  }

  @Override
  public void delete(Long id) {
    jdbcTemplate.update(DELETE_TAG_QUERY, id);
  }

  @Override
  public List<Tag> query(Specification specification) {
    SqlRequest sqlRequest = specification.createSqlRequest();
    return jdbcTemplate.query(sqlRequest.getSqlString(), sqlRequest.getRequestParameters(), getRowMapper());
  }

  private RowMapper<Tag> getRowMapper() {
    return (rs, rowNum) -> new Tag(rs.getLong(ID_COLUMN_NAME), rs.getString(NAME_COLUMN_NAME));
  }
}