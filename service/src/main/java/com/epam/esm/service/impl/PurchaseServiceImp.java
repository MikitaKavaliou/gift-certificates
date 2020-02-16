package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.PurchaseDao;
import com.epam.esm.dto.PurchaseWithCertificateDto;
import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Purchase;
import com.epam.esm.service.PurchaseService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseServiceImp implements PurchaseService {

  private final PurchaseDao purchaseDao;
  private final GiftCertificateDao giftCertificateDao;

  @Autowired
  public PurchaseServiceImp(PurchaseDao purchaseDao, GiftCertificateDao giftCertificateDao) {
    this.purchaseDao = purchaseDao;
    this.giftCertificateDao = giftCertificateDao;
  }

  @Override
  public PurchaseWithCertificateDto create(Purchase purchase, String resourceUrl) {
    GiftCertificate giftCertificate = giftCertificateDao.findById(purchase.getGiftCertificateId())
        .orElseThrow(() -> new ServerException(ExceptionType.INCORRECT_INPUT_DATA));
    purchase = purchaseDao.findBydId(purchaseDao.create(purchase))
        .orElseThrow(() -> new ServerException(ExceptionType.ERROR_CREATING_ENTITY));
    return new PurchaseWithCertificateDto(purchase, resourceUrl + giftCertificate.getId());
  }

  @Override
  public List<PurchaseWithCertificateDto> findPurchasesByUserId(Long userId, String resourceUrl) {
    List<Purchase> purchases = purchaseDao.findPurchasesByUserId(userId);
    return purchases
        .stream().map(p -> p.getGiftCertificateId() == null ? new PurchaseWithCertificateDto(p, null)
            : new PurchaseWithCertificateDto(p, resourceUrl + p.getGiftCertificateId()))
        .collect(Collectors.toList());
  }
}