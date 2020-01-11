package com.epam.esm.repository.impl;

import com.epam.esm.entity.SqlRequest;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.specification.Specification;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Implementation of GiftCertificateRepository.
 */
@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

  private static final String CREATE_CERTIFICATE_QUERY =
      "INSERT INTO gift_certificate (name, description, price, duration) VALUES (?, ?, ?, ?)";
  private static final String DELETE_CERTIFICATE_QUERY = "DELETE FROM gift_certificate WHERE gift_certificate_id = ? ";
  private static final String CREATE_XREF_TABLE_RECORD =
      "INSERT INTO tag_gift_certificate (tag_id, gift_certificate_id) VALUES (?, ?)";
  private static final String START_OF_UPDATE_QUERY = "UPDATE gift_certificate SET ";
  private static final String COMMA = ", ";
  private static final String UPDATE_NAME = "name = ? ";
  private static final String UPDATE_DESCRIPTION = "description = ? ";
  private static final String UPDATE_PRICE = "price = ? ";
  private static final String UPDATE_DURATION = "duration = ? ";
  private static final String END_OF_UPDATE_QUERY = "WHERE gift_certificate_id = ?";
  private static final String ID_COLUMN_NAME = "gift_certificate_id";
  private static final String NAME_COLUMN_NAME = "name";
  private static final String DESCRIPTION_COLUMN_NAME = "description";
  private static final String PRICE_COLUMN_NAME = "price";
  private static final String CREATE_DATE_COLUMN_NAME = "create_date";
  private static final String LAST_UPDATE_DATE_COLUMN_NAME = "last_update_date";
  private static final String DURATION_COLUMN_NAME = "duration";

  private final JdbcTemplate jdbcTemplate;
  private final Map<String, String> updateFieldsMap = new HashMap<>();

  /**
   * Instantiates a new Gift certificate repository.
   *
   * @param jdbcTemplate the jdbc template is used for execution queries.
   */
  @Autowired
  public GiftCertificateRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    initializeUpdateFieldsMap();
  }

  private void initializeUpdateFieldsMap() {
    updateFieldsMap.put(NAME_COLUMN_NAME, UPDATE_NAME);
    updateFieldsMap.put(DESCRIPTION_COLUMN_NAME, UPDATE_DESCRIPTION);
    updateFieldsMap.put(PRICE_COLUMN_NAME, UPDATE_PRICE);
    updateFieldsMap.put(DURATION_COLUMN_NAME, UPDATE_DURATION);
  }

  @Override
  public Long create(GiftCertificate giftCertificate) {
    return createGiftCertificate(giftCertificate);
  }

  @Override
  public Long update(GiftCertificate giftCertificate) {
    return updateGiftCertificate(giftCertificate);
  }

  @Override
  public Long create(GiftCertificate giftCertificate, List<Long> tagIdList) {
    Long giftCertificateId = createGiftCertificate(giftCertificate);
    createCrossReferenceTableRecords(giftCertificateId, tagIdList);
    return giftCertificateId;
  }

  @Override
  public Long update(GiftCertificate giftCertificate, List<Long> tagIdList) {
    Long giftCertificateId = updateGiftCertificate(giftCertificate);
    createCrossReferenceTableRecords(giftCertificateId, tagIdList);
    return giftCertificateId;
  }

  @SuppressWarnings("all")
  private Long createGiftCertificate(GiftCertificate giftCertificate) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection
          .prepareStatement(CREATE_CERTIFICATE_QUERY, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, giftCertificate.getName());
      ps.setString(2, giftCertificate.getDescription());
      ps.setBigDecimal(3, giftCertificate.getPrice());
      ps.setObject(4, giftCertificate.getDuration());
      return ps;
    }, keyHolder);
    return keyHolder.getKeys().size() > 1 ?
        Long.valueOf((Integer) keyHolder.getKeys().get("gift_certificate_id")) : keyHolder.getKey().longValue();
  }

  private Long updateGiftCertificate(GiftCertificate giftCertificate) {
    SqlRequest request = buildUpdateQuery(giftCertificate);
    if (request.getRequestParameters().length > 1) {
      jdbcTemplate.update(request.getSqlString(), request.getRequestParameters());
    }
    return giftCertificate.getId();
  }

  @Override
  public void delete(Long id) {
    jdbcTemplate.update(DELETE_CERTIFICATE_QUERY, id);
  }

  @Override
  public List<GiftCertificate> query(Specification specification) {
    SqlRequest sqlRequest = specification.createSqlRequest();
    return jdbcTemplate.query(sqlRequest.getSqlString(), sqlRequest.getRequestParameters(), getRowMapper());
  }

  private SqlRequest buildUpdateQuery(GiftCertificate giftCertificate) {
    StringBuilder sb = new StringBuilder(START_OF_UPDATE_QUERY);
    List<Object> parameters = new ArrayList<>();
    sb.append(createFieldUpdateString(NAME_COLUMN_NAME, giftCertificate.getName(), parameters));
    sb.append(createFieldUpdateString(DESCRIPTION_COLUMN_NAME, giftCertificate.getDescription(), parameters));
    sb.append(createFieldUpdateString(PRICE_COLUMN_NAME, giftCertificate.getPrice(), parameters));
    sb.append(createFieldUpdateString(DURATION_COLUMN_NAME, giftCertificate.getDuration(), parameters));
    sb.append(END_OF_UPDATE_QUERY);
    parameters.add(giftCertificate.getId());
    return new SqlRequest(sb.toString(), parameters.toArray());
  }

  private StringBuilder createFieldUpdateString(String fieldName, Object fieldValue, List<Object> parameters) {
    StringBuilder sb = new StringBuilder();
    if (fieldValue != null) {
      if (!parameters.isEmpty()) {
        sb.append(COMMA);
      }
      sb.append(updateFieldsMap.get(fieldName));
      parameters.add(fieldValue);
    }
    return sb;
  }

  private void createCrossReferenceTableRecords(Long certificateId, List<Long> tagIdList) {
    jdbcTemplate.batchUpdate(CREATE_XREF_TABLE_RECORD,
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setLong(1, tagIdList.get(i));
            ps.setLong(2, certificateId);
          }

          @Override
          public int getBatchSize() {
            return tagIdList.size();
          }
        });
  }

  private RowMapper<GiftCertificate> getRowMapper() {
    return (rs, rowNum) -> GiftCertificate.builder()
        .id(rs.getLong(ID_COLUMN_NAME))
        .name(rs.getString(NAME_COLUMN_NAME))
        .description(rs.getString(DESCRIPTION_COLUMN_NAME))
        .price(rs.getBigDecimal(PRICE_COLUMN_NAME))
        .createDate(rs.getTimestamp(CREATE_DATE_COLUMN_NAME).toLocalDateTime().atZone(ZoneOffset.systemDefault()))
        .lastUpdateDate(
            rs.getTimestamp(LAST_UPDATE_DATE_COLUMN_NAME).toLocalDateTime().atZone(ZoneOffset.systemDefault()))
        .duration(rs.getInt(DURATION_COLUMN_NAME))
        .build();
  }
}