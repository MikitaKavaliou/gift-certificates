package com.epam.esm.validation;

import com.epam.esm.dto.GiftCertificateUpdateDto;
import com.epam.esm.dto.GiftCertificateWithTagsDto;
import com.epam.esm.model.Tag;
import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class GiftCertificateValidator {

  private static final int MAX_CERTIFICATE_NAME_LENGTH = 30;
  private static final int MAX_CERTIFICATE_DESCRIPTION_LENGTH = 1000;
  private static final double MIN_CERTIFICATE_PRICE = 0;
  private static final double MAX_CERTIFICATE_PRICE = 10000;
  private static final int MIN_CERTIFICATE_DURATION = 0;
  private static final int MAX_CERTIFICATE_DURATION = 10000;

  private GiftCertificateValidator() {

  }

  public static boolean isValidGiftCertificateValuesForCreate(GiftCertificateWithTagsDto certificate) {
    return StringUtils.isNotBlank(certificate.getName()) && isValidGiftCertificateName(certificate.getName())
        && StringUtils.isNotBlank(certificate.getDescription())
        && isValidGiftCertificateDescription(certificate.getDescription())
        && certificate.getPrice() != null && isValidCertificatePrice(certificate.getPrice())
        && (certificate.getDuration() == null || isValidCertificateDuration(certificate.getDuration()))
        && isValidTagList(certificate.getTags());
  }

  public static boolean isValidGiftCertificateValuesForUpdate(GiftCertificateUpdateDto certificate) {
    return (certificate.getName() == null || isValidGiftCertificateName(certificate.getName())
        && (certificate.getDescription() == null || isValidGiftCertificateDescription(certificate.getDescription()))
        && (certificate.getPrice() == null || isValidCertificatePrice(certificate.getPrice()))
        && (certificate.getDuration() == null || isValidCertificateDuration(certificate.getDuration()))
        && isValidTagList(certificate.getTagsForAdding()) && isValidTagList(certificate.getTagsForDeletion()));
  }

  public static boolean isValidGiftCertificateName(String name) {
    return !name.isEmpty() && name.length() <= MAX_CERTIFICATE_NAME_LENGTH;
  }

  public static boolean isValidGiftCertificateDescription(String description) {
    return !description.isEmpty() && description.length() <= MAX_CERTIFICATE_DESCRIPTION_LENGTH;
  }

  public static boolean isValidCertificatePrice(BigDecimal price) {
    return price.doubleValue() >= MIN_CERTIFICATE_PRICE && price.doubleValue() <= MAX_CERTIFICATE_PRICE;
  }

  public static boolean isValidCertificateDuration(Integer duration) {
    return duration >= MIN_CERTIFICATE_DURATION && duration <= MAX_CERTIFICATE_DURATION;
  }

  public static boolean isValidTagList(List<Tag> tags) {
    return tags == null || tags.isEmpty() || tags.stream().allMatch(TagValidator::isValidTag);
  }
}