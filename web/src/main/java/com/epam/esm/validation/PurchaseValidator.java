package com.epam.esm.validation;

import com.epam.esm.model.Purchase;

public class PurchaseValidator {

  private static final long MIN_GIFT_CERTIFICATE_ID = 1;

  private PurchaseValidator() {

  }

  public static boolean isValidPurchaseForCreation(Purchase purchase) {
    return purchase.getGiftCertificateId() != null && purchase.getGiftCertificateId() >= MIN_GIFT_CERTIFICATE_ID;
  }
}