package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GiftCertificateDao {

  Long create(GiftCertificate giftCertificate, List<Long> tagIdsForAddingToCertificate);

  Optional<GiftCertificate> findById(Long id);

  List<GiftCertificate> findByCriteria(Map<String, String> parameters);

  List<GiftCertificate> findByUserId(Long userId);

  void update(GiftCertificate giftCertificate, List<Long> tagIdsForAddingToCertificate,
      List<Long> tagIdsForDeletingFromCertificate);

  int delete(Long id);

  void updatePrice(Long id, BigDecimal price);
}