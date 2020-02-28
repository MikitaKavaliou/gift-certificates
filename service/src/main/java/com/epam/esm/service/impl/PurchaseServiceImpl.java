package com.epam.esm.service.impl;

import com.epam.esm.dto.PurchaseWithCertificateDto;
import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.mapper.PurchaseMapper;
import com.epam.esm.model.Purchase;
import com.epam.esm.service.PurchaseService;
import com.epam.esm.util.PaginationUtil;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * The type Purchase service.
 */
@Service
public class PurchaseServiceImpl implements PurchaseService {

  private final PurchaseMapper purchaseMapper;

  /**
   * Instantiates a new Purchase service.
   *
   * @param purchaseMapper the purchase mapper
   */
  @Autowired
  public PurchaseServiceImpl(PurchaseMapper purchaseMapper) {
    this.purchaseMapper = purchaseMapper;
  }

  @Override
  public PurchaseWithCertificateDto create(Purchase purchase, String resourceUrl) {
    purchase = purchaseMapper.selectById(insertPurchase(purchase))
        .orElseThrow(() -> new ServerException(ExceptionType.ERROR_CREATING_ENTITY));
    return new PurchaseWithCertificateDto(purchase, resourceUrl + purchase.getGiftCertificateId());
  }

  private Long insertPurchase(Purchase purchase) {
    try {
      purchaseMapper.insert(purchase);
      return purchase.getId();
    } catch (DataIntegrityViolationException ex) {
      throw new ServerException(ExceptionType.INCORRECT_INPUT_DATA);
    }
  }

  @Override
  public List<PurchaseWithCertificateDto> findPurchasesByUserId(Long userId, String resourceUrl,
      Map<String, String> parameters) {
    List<Purchase> purchases = purchaseMapper.selectByUserId(userId, PaginationUtil.createRowBounds(parameters));
    return purchases
        .stream().map(p -> p.getGiftCertificateId() == null ? new PurchaseWithCertificateDto(p, null)
            : new PurchaseWithCertificateDto(p, resourceUrl + p.getGiftCertificateId()))
        .collect(Collectors.toList());
  }
}