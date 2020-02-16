package com.epam.esm.service.impl;

import com.epam.esm.dto.PurchaseWithCertificateDto;
import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.mapper.PurchaseMapper;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Purchase;
import com.epam.esm.service.PurchaseService;
import com.epam.esm.util.PaginationTool;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseServiceImp implements PurchaseService {

  private final PurchaseMapper purchaseMapper;
  private final GiftCertificateMapper giftCertificateMapper;

  @Autowired
  public PurchaseServiceImp(PurchaseMapper purchaseMapper,
      GiftCertificateMapper giftCertificateMapper) {

    this.purchaseMapper = purchaseMapper;
    this.giftCertificateMapper = giftCertificateMapper;
  }

  @Override
  public PurchaseWithCertificateDto create(Purchase purchase, String resourceUrl) {
    GiftCertificate giftCertificate = giftCertificateMapper.selectById(purchase.getGiftCertificateId())
        .orElseThrow(() -> new ServerException(ExceptionType.INCORRECT_INPUT_DATA));
    purchase = purchaseMapper.selectById(insertPurchase(purchase))
        .orElseThrow(() -> new ServerException(ExceptionType.ERROR_CREATING_ENTITY));
    return new PurchaseWithCertificateDto(purchase, resourceUrl + giftCertificate.getId());
  }

  private Long insertPurchase(Purchase purchase) {
    purchaseMapper.insert(purchase);
    return purchase.getId();
  }

  @Override
  public List<PurchaseWithCertificateDto> findPurchasesByUserId(Long userId, String resourceUrl,
      Map<String, String> parameters) {
    List<Purchase> purchases = purchaseMapper.selectByUserId(userId, PaginationTool.createRowBounds(parameters));
    return purchases
        .stream().map(p -> p.getGiftCertificateId() == null ? new PurchaseWithCertificateDto(p, null)
            : new PurchaseWithCertificateDto(p, resourceUrl + p.getGiftCertificateId()))
        .collect(Collectors.toList());
  }
}