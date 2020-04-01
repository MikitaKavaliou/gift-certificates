package com.epam.esm.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.epam.esm.dto.EntityListDto;
import com.epam.esm.dto.PurchaseWithCertificateDto;
import com.epam.esm.exception.ServerException;
import com.epam.esm.mapper.PurchaseMapper;
import com.epam.esm.model.Purchase;
import com.epam.esm.service.impl.PurchaseServiceImpl;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;

@RunWith(MockitoJUnitRunner.class)
public class PurchaseServiceTest {

  private static Purchase firstPurchase;
  private static Purchase secondPurchase;
  private static List<Purchase> purchases;
  private static String resourceUrl;
  private static PurchaseWithCertificateDto purchaseDto;
  @Mock
  private PurchaseMapper purchaseMapper;
  @InjectMocks
  private PurchaseServiceImpl purchaseService;

  @BeforeClass
  public static void beforeClass() {
    firstPurchase = new Purchase(1L, 1L, BigDecimal.ONE, null, 1L);
    secondPurchase = new Purchase(1L, 1L, BigDecimal.ONE, null, null);
    purchases = Arrays.asList(firstPurchase, secondPurchase);
    resourceUrl = "someUrl";
    purchaseDto = new PurchaseWithCertificateDto(firstPurchase, resourceUrl + firstPurchase.getId());

  }

  @Test
  public void createTestCorrectReturnsPurchase() {
    when(purchaseMapper.selectById(any())).thenReturn(Optional.of(firstPurchase));
    PurchaseWithCertificateDto actual = purchaseService.create(firstPurchase, resourceUrl);
    Assert.assertTrue(new ReflectionEquals(actual).matches(purchaseDto));
  }

  @Test(expected = ServerException.class)
  public void createTestIncorrectCertificateIdThrowsException() {
    doThrow(DataIntegrityViolationException.class).when(purchaseMapper).insert(any());
    purchaseService.create(firstPurchase, resourceUrl);
  }

  @Test
  public void findPurchasesByUserIdTest() {
    when(purchaseMapper.selectByUserId(any(), any())).thenReturn(purchases);
    EntityListDto<PurchaseWithCertificateDto> actual = purchaseService
        .findPurchasesByUserId(1L, resourceUrl, new HashMap<>());
    EntityListDto<PurchaseWithCertificateDto> expected =
        new EntityListDto<>(Arrays.asList(new PurchaseWithCertificateDto(firstPurchase,
                resourceUrl + firstPurchase.getGiftCertificateId()),
            new PurchaseWithCertificateDto(secondPurchase, null)));
    Assert.assertEquals(expected.getGiftCertificatesWithTags().size(), actual.getGiftCertificatesWithTags().size());
    Assert.assertTrue(new ReflectionEquals(actual.getGiftCertificatesWithTags().get(0))
        .matches(expected.getGiftCertificatesWithTags().get(0)));
  }
}