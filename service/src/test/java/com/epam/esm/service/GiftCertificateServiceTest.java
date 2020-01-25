package com.epam.esm.service;

import com.epam.esm.entity.CertificateWithTags;
import com.epam.esm.exception.ServerException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.specification.impl.certificate.CertificateIdSpecification;
import com.epam.esm.repository.specification.impl.certificate.CertificatesCriteriaSpecification;
import com.epam.esm.repository.specification.impl.tag.TagCertificateIdSpecification;
import com.epam.esm.repository.specification.impl.tag.TagNameSpecification;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class GiftCertificateServiceTest {

  private static final long id = 1L;
  private static GiftCertificate certificate;
  private static List<GiftCertificate> certificates;
  private static Tag tag;
  private static List<Tag> tags;
  private static CertificateWithTags certificateWithTags;

  @Mock
  private TagRepository tagRepository;
  @Mock
  private GiftCertificateRepository certificateRepository;
  @InjectMocks
  private GiftCertificateServiceImpl certificateService;

  @BeforeClass
  public static void setUp() {
    certificate = GiftCertificate.builder()
        .description("description")
        .name("name")
        .price(BigDecimal.ONE)
        .duration(1)
        .build();
    certificates = Collections.singletonList(certificate);
    tag = new Tag();
    tags = Collections.singletonList(tag);
    certificateWithTags = new CertificateWithTags(certificate, tags);
  }

  @Test
  public void findByIdFoundCertificate() {
    Mockito.when(certificateRepository.query(Mockito.any(CertificateIdSpecification.class)))
        .thenReturn(certificates);
    Mockito.when(tagRepository.query(Mockito.any(TagCertificateIdSpecification.class))).thenReturn(tags);
    CertificateWithTags actual = certificateService.findById(1L);
    CertificateWithTags expected = new CertificateWithTags(certificate, tags);
    Assert.assertEquals(expected, actual);
  }

  @Test(expected = ServerException.class)
  public void findById() {
    Mockito.when(certificateRepository.query(Mockito.any(CertificateIdSpecification.class)))
        .thenReturn(new ArrayList<>());
    certificateService.findById(id);
  }

  @Test
  public void createGiftCertificateWithNotNullDbExistingTags() {
    Mockito.when(tagRepository.query(Mockito.any(TagNameSpecification.class))).thenReturn(tags);
    Mockito.when(certificateRepository.create(Mockito.any(GiftCertificate.class), Mockito.any(List.class)))
        .thenReturn(id);
    long actualId = certificateService.create(certificateWithTags);
    Assert.assertEquals(id, actualId);
  }

  @Test
  public void createGiftCertificateWithNotNullNotExistingTags() {
    Mockito.when(tagRepository.query(Mockito.any(TagNameSpecification.class))).thenReturn(new ArrayList<>());
    Mockito.when(tagRepository.create(tag)).thenReturn(1L);
    Mockito.when(certificateRepository.create(Mockito.any(GiftCertificate.class), Mockito.any(List.class)))
        .thenReturn(id);
    long actualId = certificateService.create(certificateWithTags);
    Assert.assertEquals(id, actualId);
  }

  @Test
  public void createGiftCertificateWithNullTags() {
    CertificateWithTags certificateWithTags = new CertificateWithTags(certificate, null);
    Mockito.when(certificateRepository.create(certificate)).thenReturn(id);
    long actualId = certificateService.create(certificateWithTags);
    Assert.assertEquals(id, actualId);
  }

  @Test
  public void updateGiftCertificateWithNotNullDbExistingTags() {
    Mockito.when(tagRepository.query(Mockito.any(TagNameSpecification.class))).thenReturn(tags);
    Mockito.when(certificateRepository.update(Mockito.any(GiftCertificate.class), Mockito.any(List.class)))
        .thenReturn(id);
    long actualId = certificateService.update(certificateWithTags);
    Assert.assertEquals(id, actualId);
  }

  @Test
  public void updateGiftCertificateWithNotNullNotExistingTags() {
    Mockito.when(tagRepository.query(Mockito.any(TagNameSpecification.class))).thenReturn(new ArrayList<>());
    Mockito.when(tagRepository.create(tag)).thenReturn(1L);
    Mockito.when(certificateRepository.update(Mockito.any(GiftCertificate.class), Mockito.any(List.class)))
        .thenReturn(id);
    long actualId = certificateService.update(certificateWithTags);
    Assert.assertEquals(id, actualId);
  }

  @Test
  public void updateGiftCertificateWithNullTags() {
    CertificateWithTags certificateWithTags = new CertificateWithTags(certificates.get(0), null);
    Mockito.when(certificateRepository.update(certificates.get(0))).thenReturn(id);
    long actualId = certificateService.update(certificateWithTags);
    Assert.assertEquals(id, actualId);
  }

  @Test
  public void findCertificatesWithTagsFoundCertificateWithTag() {
    Map<String, String> requestCriteria = new HashMap<>();
    Mockito.when(certificateRepository.query(Mockito.any(CertificatesCriteriaSpecification.class)))
        .thenReturn(certificates);
    Mockito.when(tagRepository.query(Mockito.any(TagCertificateIdSpecification.class))).thenReturn(tags);
    List<CertificateWithTags> actualList = certificateService.findCertificatesWithTags(requestCriteria);
    CertificateWithTags certificateWithTags = new CertificateWithTags(certificate, tags);
    List<CertificateWithTags> expectedList = Collections.singletonList(certificateWithTags);
    Assert.assertEquals(expectedList, actualList);
  }

  @Test
  public void deleteCorrectMethodCall() {
    long tagId = 1L;
    certificateService.delete(tagId);
    Mockito.verify(certificateRepository).delete(1L);
  }
}