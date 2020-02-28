package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateUpdateDto;
import com.epam.esm.dto.GiftCertificateWithTagsDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * The interface Gift certificate service. Interface defines specific operations with GiftCertificates.
 */
public interface GiftCertificateService {

  /**
   * Create gift certificate with tags, returns created certificate with tags.
   *
   * @param giftCertificateWithTagsDto the gift certificate with tags dto
   * @return the created gift certificate with tags
   */
  GiftCertificateWithTagsDto create(GiftCertificateWithTagsDto giftCertificateWithTagsDto);

  /**
   * Find certificate by id, returns found certificate with tags.
   *
   * @param id the certificate id
   * @return the gift certificate with tags
   */
  GiftCertificateWithTagsDto findById(Long id);

  /**
   * Find certificates with tags by criteria, returns found list of found certificates with tags.
   *
   * @param requestCriteria the request criteria
   * @return the list of certificates with tags
   */
  List<GiftCertificateWithTagsDto> findByCriteria(Map<String, String> requestCriteria);

  /**
   * Find by user id, returns list of certificates with tags.
   *
   * @param userId     the user id
   * @param parameters the parameters
   * @return the list of certificates with tags
   */
  List<GiftCertificateWithTagsDto> findByUserId(Long userId, Map<String, String> parameters);

  /**
   * Update certificate with passed tags, returns updated certificate with tags.
   *
   * @param giftCertificateId        the gift certificate id
   * @param giftCertificateUpdateDto the gift certificate update dto
   * @return updated certificate with tags
   */
  GiftCertificateWithTagsDto update(Long giftCertificateId, GiftCertificateUpdateDto giftCertificateUpdateDto);

  /**
   * Update price, returns updated gift certificate with tags .
   *
   * @param id    the id
   * @param price the price
   * @return the gift certificate with tags dto
   */
  GiftCertificateWithTagsDto updatePrice(Long id, BigDecimal price);

  /**
   * Delete certificate by id, returns int number of deleted rows.
   *
   * @param id the certificate id
   * @return the int number of deleted rows
   */
  int delete(Long id);
}