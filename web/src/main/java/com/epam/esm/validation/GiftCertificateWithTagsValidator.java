package com.epam.esm.validation;

import com.epam.esm.entity.GiftCertificateWithTags;
import com.epam.esm.model.Tag;
import java.math.BigDecimal;
import java.util.List;

public class GiftCertificateWithTagsValidator {

  private static final int MAX_CERTIFICATE_NAME_LENGTH = 50;
  private static final int MAX_CERTIFICATE_DESCRIPTION_LENGTH = 200;
  private static final double MIN_CERTIFICATE_PRICE = 0;
  private static final int MIN_CERTIFICATE_DURATION = 1;

  private GiftCertificateWithTagsValidator() {

  }

  public static boolean isValidGiftCertificate(GiftCertificateWithTags certificate) {
    return isValidGiftCertificateName(certificate.getName())
        && isValidGiftCertificateDescription(certificate.getDescription())
        && isValidCertificatePrice(certificate.getPrice())
        && isValidCertificateDuration(certificate.getDuration())
        && isValidTags(certificate.getTags())
        && isValidTags(certificate.getTagsForDeletion());
  }

  private static boolean isValidGiftCertificateName(String name) {
    return name != null && !name.isEmpty() && name.length() <= MAX_CERTIFICATE_NAME_LENGTH;
  }

  private static boolean isValidGiftCertificateDescription(String description) {
    return description != null && !description.isEmpty() && description.length() <= MAX_CERTIFICATE_DESCRIPTION_LENGTH;
  }

  private static boolean isValidCertificatePrice(BigDecimal price) {
    return price != null && price.doubleValue() >= MIN_CERTIFICATE_PRICE;
  }

  private static boolean isValidCertificateDuration(Integer duration) {
    return duration != null && duration >= MIN_CERTIFICATE_DURATION;
  }

  private static boolean isValidTags(List<Tag> tags) {
    return tags == null || tags.isEmpty() || tags.stream().allMatch(TagValidator::isValidTag);
  }
}