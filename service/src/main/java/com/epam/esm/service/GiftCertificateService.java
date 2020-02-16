package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateWithTagsDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * The interface Gift certificate service. Interface defines specific operations with GiftCertificates.
 */
public interface GiftCertificateService {

  GiftCertificateWithTagsDto create(GiftCertificateWithTagsDto giftCertificateWithTagsDto);

  GiftCertificateWithTagsDto findById(Long id);

  /**
   * Find certificates with tags returns found entities list.
   *
   * @param requestCriteria the request criteria
   * @return the list of found entities
   */
  List<GiftCertificateWithTagsDto> findByCriteria(Map<String, String> requestCriteria);

  List<GiftCertificateWithTagsDto> findByUserId(Long userId, Map<String, String> parameters);

  /**
   * Update certificate with passed tags returns id of update entity long.
   *
   * @param giftCertificateWithTagsDto the certificate with tags
   * @return the long id of created entity
   */
  GiftCertificateWithTagsDto update(GiftCertificateWithTagsDto giftCertificateWithTagsDto, String tagAction);

  /**
   * Update price gift certificate with tags dto.
   *
   * @param id    the id
   * @param price the price
   * @return the gift certificate with tags dto
   */
  GiftCertificateWithTagsDto updatePrice(Long id, BigDecimal price);

  int delete(Long id);
}