package com.epam.esm.repository.specification.impl.tag;

import com.epam.esm.entity.SqlRequest;
import com.epam.esm.repository.specification.Specification;

public class TagIdCertificateIdSpecification implements Specification {


  private static final String SELECT_BY_CERTIFICATE_ID_QUERY = "SELECT T.tag_id, T.name FROM tag T"
      + " JOIN tag_gift_certificate TG ON T.tag_id=TG.tag_id WHERE TG.gift_certificate_id = ? and TG.tag_id = ?";

  private Long certificateId;
  private Long tagId;

  private TagIdCertificateIdSpecification() {
  }

  public TagIdCertificateIdSpecification(Long certificateId, Long tagId) {
    this.certificateId = certificateId;
    this.tagId = tagId;
  }

  @Override
  public SqlRequest createSqlRequest() {
    return new SqlRequest(SELECT_BY_CERTIFICATE_ID_QUERY, new Object[]{certificateId, tagId});
  }
}