package com.epam.esm.repository;

import com.epam.esm.model.GiftCertificate;
import java.util.List;

/**
 * The interface Gift certificate repository. Interface defines specific methods for working with `gift_certificate`
 * database table.
 */
public interface GiftCertificateRepository extends GenericRepository<GiftCertificate, Long> {

  /**
   * Create gift certificates returns id long.
   *
   * @param giftCertificate the gift certificate
   * @param tagIdList       the tag id list
   * @return the id of created entity long
   */
  Long create(GiftCertificate giftCertificate, List<Long> tagIdList);

  /**
   * Update gift certificates returns id long.
   *
   * @param giftCertificate the gift certificate
   * @return the id of updated entity long
   */
  Long update(GiftCertificate giftCertificate);

  /**
   * Update gift certificates with tags returns id long.
   *
   * @param giftCertificate the gift giftCertificate
   * @param tagIdList       the tag id list
   * @return the id of updated entity long
   */
  Long update(GiftCertificate giftCertificate, List<Long> tagIdList);
}