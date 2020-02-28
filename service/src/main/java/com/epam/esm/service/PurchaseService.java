package com.epam.esm.service;

import com.epam.esm.dto.PurchaseWithCertificateDto;
import com.epam.esm.model.Purchase;
import java.util.List;
import java.util.Map;

/**
 * The interface Purchase service. Interface defines specific operations with Purchases.
 */
public interface PurchaseService {

  /**
   * Create purchase with certificate dto, returns created purchase with certificate.
   *
   * @param purchase    the purchase for creation
   * @param resourceUrl the resource basic url for creation certificate specific urls
   * @return the purchase with certificate dto
   */
  PurchaseWithCertificateDto create(Purchase purchase, String resourceUrl);

  /**
   * Find purchases by user id list., returns list of purchases with certificates.
   *
   * @param userId      the user id
   * @param resourceUrl the resource basic url for creation certificate specific urls
   * @param parameters  the request parameters
   * @return the list if purchases with certificate dto
   */
  List<PurchaseWithCertificateDto> findPurchasesByUserId(Long userId, String resourceUrl,
      Map<String, String> parameters);
}