package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import java.util.List;
import java.util.Map;

public interface GiftCertificateDao extends GenericDao<GiftCertificate, Long> {

  Long create(GiftCertificate giftCertificate, List<Long> tagIdsForAddingToCertificate);

  void update(GiftCertificate giftCertificate, List<Long> tagIdsForAddingToCertificate,
      List<Long> tagIdsForDeletingFromCertificate);

  List<GiftCertificate> findCertificatesByCriteria(Map<String, String> parameters);
}