package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateWithTags;
import java.util.List;
import java.util.Map;


/**
 * The interface Gift certificate service. Interface defines specific operations with GiftCertificates.
 */
public interface GiftCertificateService extends Service<GiftCertificateWithTags, Long> {

  /**
   * Update certificate with passed tags returns id of update entity long.
   *
   * @param giftCertificateWithTags the certificate with tags
   * @return the long id of created entity
   */
  Long update(GiftCertificateWithTags giftCertificateWithTags);

  /**
   * Find certificates with tags returns found entities list.
   *
   * @param requestCriteria the request criteria
   * @return the list of found entities
   */
  List<GiftCertificateWithTags> findCertificatesWithTags(Map<String, String> requestCriteria);
}