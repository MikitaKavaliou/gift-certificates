package com.epam.esm.service;

import com.epam.esm.entity.CertificateWithTags;
import java.util.List;
import java.util.Map;


/**
 * The interface Gift certificate service. Interface defines specific operations with GiftCertificates.
 */
public interface GiftCertificateService extends Service<CertificateWithTags, Long> {

  /**
   * Update certificate with passed tags returns id of update entity long.
   *
   * @param certificateWithTags the certificate with tags
   * @return the long id of created entity
   */
  Long update(CertificateWithTags certificateWithTags);

  /**
   * Find certificates with tags returns found entities list.
   *
   * @param requestCriteria the request criteria
   * @return the list of found entities
   */
  List<CertificateWithTags> findCertificatesWithTags(Map<String, String> requestCriteria);
}