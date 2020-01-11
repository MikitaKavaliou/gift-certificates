package com.epam.esm.repository.specification.impl.certificate;

import com.epam.esm.entity.SqlRequest;
import com.epam.esm.repository.specification.Specification;

/**
 * The type Certificate id specification. Creates database query for finding a certificate by id.
 */
public class CertificateIdSpecification implements Specification {

  private static final String SELECT_BY_ID_QUERY =
      "SELECT gift_certificate_id, name, description, price, create_date, "
          + "last_update_date, duration FROM gift_certificate WHERE gift_certificate_id = ?";

  private Long id;

  private CertificateIdSpecification() {
  }

  /**
   * Instantiates a new Certificate id specification.
   *
   * @param id the id
   */
  public CertificateIdSpecification(Long id) {
    this.id = id;
  }

  @Override
  public SqlRequest createSqlRequest() {
    return new SqlRequest(SELECT_BY_ID_QUERY, new Long[]{id});
  }
}