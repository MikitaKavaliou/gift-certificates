package com.epam.esm.provider;

import com.epam.esm.model.GiftCertificate;
import org.apache.ibatis.jdbc.SQL;

public class GiftCertificateQueryProvider {

  public String updateQuery(final GiftCertificate giftCertificate) {
    return new SQL() {{
      UPDATE("gift_certificate");
      if (giftCertificate.getName() != null) {
        SET("name=#{name}");
      }
      if (giftCertificate.getDescription() != null) {
        SET("description=#{description}");
      }
      if (giftCertificate.getPrice() != null) {
        SET("price=#{price}");
      }
      if (giftCertificate.getDuration() != null) {
        SET("duration=#{duration}");
      }
      WHERE("gift_certificate_id=#{id}");
    }}.toString();
  }
}