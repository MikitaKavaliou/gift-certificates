package com.epam.esm.repository.specification.impl.tag;

import com.epam.esm.entity.SqlRequest;
import com.epam.esm.repository.specification.Specification;

/**
 * The type Tag certificate id specification. Creates database query for finding tags with certificate id.
 */
public class TagCertificateIdSpecification implements Specification {

  private static final String SELECT_BY_CERTIFICATE_ID_QUERY = "SELECT T.tag_id, T.name FROM tag T"
      + " JOIN tag_gift_certificate TG ON T.tag_id=TG.tag_id WHERE TG.gift_certificate_id = ?";
  private Long certificateId;

  private TagCertificateIdSpecification() {
  }

  /**
   * Instantiates a new Tag certificate id specification.
   *
   * @param certificateId the certificate id
   */
  public TagCertificateIdSpecification(Long certificateId) {
    this.certificateId = certificateId;
  }

  @Override
  public SqlRequest createSqlRequest() {
    return new SqlRequest(SELECT_BY_CERTIFICATE_ID_QUERY, new Object[]{certificateId});
  }
}