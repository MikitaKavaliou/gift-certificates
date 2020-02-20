package com.epam.esm.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.esm.dto.GiftCertificateWithTagsDto;
import com.epam.esm.exception.ServerException;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class GiftCertificateServiceTest {

  private static final long id = 1L;
  private static GiftCertificate certificate;
  private static List<GiftCertificate> certificates;
  private static Tag tag;
  private static List<Tag> tags;
  private static GiftCertificateWithTagsDto giftCertificateWithTags;

  @Mock
  private TagMapper tagMapper;
  @Mock
  private GiftCertificateMapper certificateMapper;
  @InjectMocks
  private GiftCertificateServiceImpl certificateService;

  @BeforeClass
  public static void beforeClass() {
    certificate = new GiftCertificate(id, "name", "description", BigDecimal.valueOf(5),
        null, null, 4);
    certificates = Collections.singletonList(certificate);
    tag = new Tag(id, "tagName");
    tags = Collections.singletonList(tag);
    giftCertificateWithTags = new GiftCertificateWithTagsDto(certificate, tags);
  }

  @Test
  public void findByIdTestFoundGiftCertificateWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    GiftCertificateWithTagsDto actual = certificateService.findById(id);
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test(expected = ServerException.class)
  public void findByIdTestThrowsException() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.empty());
    certificateService.findById(id);
  }

  @Test
  public void deleteTestCorrectMethodCall() {
    long tagId = 1L;
    certificateService.delete(tagId);
    verify(certificateMapper).delete(1L);
  }

  @Test
  public void createTestCreateCertificateAddExistedTagReturnsCertificatesWithTags() {
    when(tagMapper.selectByNames(any())).thenReturn(tags);
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    GiftCertificateWithTagsDto actual = certificateService.create(giftCertificateWithTags);
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test
  public void createTestCreateCertificateInsertNewTagReturnsCertificatesWithTags() {
    when(tagMapper.selectByNames(any())).thenReturn(new ArrayList<>());
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    GiftCertificateWithTagsDto actual = certificateService.create(giftCertificateWithTags);
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test
  public void createTestCreateCertificateWithNullTagsReturnsCertificatesWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    List<Tag> emptyTagList = new ArrayList<>();
    when(tagMapper.selectByCertificateId(any())).thenReturn(emptyTagList);
    GiftCertificateWithTagsDto expected = new GiftCertificateWithTagsDto(certificate, emptyTagList);
    GiftCertificateWithTagsDto actual = certificateService.create(new GiftCertificateWithTagsDto(certificate, null));
    Assert.assertTrue(new ReflectionEquals(actual).matches(expected));
  }

  @Test
  public void createTestCreateCertificateWithEmptyTagsReturnsCertificatesWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    List<Tag> emptyTagList = new ArrayList<>();
    when(tagMapper.selectByCertificateId(any())).thenReturn(emptyTagList);
    GiftCertificateWithTagsDto expected = new GiftCertificateWithTagsDto(certificate, emptyTagList);
    GiftCertificateWithTagsDto actual = certificateService
        .create(new GiftCertificateWithTagsDto(certificate, new ArrayList<>()));
    Assert.assertTrue(new ReflectionEquals(actual).matches(expected));
  }

  @Test(expected = ServerException.class)
  public void createTestErrorCreatingCertificateThrowsException() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.empty());
    certificateService.create(giftCertificateWithTags);
  }

  @Test(expected = ServerException.class)
  public void updateTestErrorAddingTagToNotExistingCertificateThrowsException() {
    doThrow(DataIntegrityViolationException.class).when(certificateMapper).insertAssociativeRecords(any(), any());
    certificateService.update(giftCertificateWithTags, "add");
  }

  @Test(expected = ServerException.class)
  public void updateTestErrorFindingNotExistedCertificateIdThrowsException() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.empty());
    certificateService.update(new GiftCertificateWithTagsDto(), "delete");
  }

  @Test
  public void updateTestUpdateNameAndNullTagsReturnsCertificateWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    GiftCertificateWithTagsDto certificate = new GiftCertificateWithTagsDto();
    certificate.setName("name");
    GiftCertificateWithTagsDto actual = certificateService.update(certificate, "delete");
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test
  public void updateTestUpdateDescriptionAndNullTagsReturnsCertificateWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    GiftCertificateWithTagsDto certificate = new GiftCertificateWithTagsDto();
    certificate.setDescription("description");
    GiftCertificateWithTagsDto actual = certificateService.update(certificate, "delete");
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test
  public void updateTestUpdateDurationAndNullTagsReturnsCertificateWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    GiftCertificateWithTagsDto certificate = new GiftCertificateWithTagsDto();
    certificate.setDuration(4);
    GiftCertificateWithTagsDto actual = certificateService.update(certificate, "delete");
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test
  public void updateTestUpdatePriceAndNullTagsReturnsCertificateWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    GiftCertificateWithTagsDto giftCertificateWithTagsDto = new GiftCertificateWithTagsDto();
    giftCertificateWithTagsDto.setTags(new ArrayList<>());
    giftCertificateWithTagsDto.setPrice(BigDecimal.valueOf(10));
    GiftCertificateWithTagsDto actual = certificateService.update(giftCertificateWithTagsDto, "delete");
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test
  public void updateTestEmptyTagsReturnsCertificateWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    GiftCertificateWithTagsDto giftCertificateWithTagsDto = new GiftCertificateWithTagsDto();
    giftCertificateWithTagsDto.setTags(new ArrayList<>());
    GiftCertificateWithTagsDto actual = certificateService.update(giftCertificateWithTagsDto, "add");
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test
  public void updateTestUpdateCertificateFieldsAndWrongActionForTagsReturnsCertificateWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    GiftCertificateWithTagsDto actual = certificateService.update(giftCertificateWithTags, "wrongAction");
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test
  public void updateTestUpdateCertificateFieldsAndAddNewTagsToCertificateReturnsCertificateWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    when(tagMapper.selectByNames(any())).thenReturn(Collections.singletonList(tag));
    when(tagMapper.selectTagIdListByTagIdSetAndCertificateId(any(), any())).thenReturn(new ArrayList<>());
    GiftCertificateWithTagsDto actual = certificateService.update(giftCertificateWithTags, "add");
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test
  public void updateTestUpdateCertificateFieldsAndAddExistedTagsToCertificateReturnsCertificateWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    when(tagMapper.selectByNames(any())).thenReturn(Collections.singletonList(tag));
    when(tagMapper.selectTagIdListByTagIdSetAndCertificateId(any(), any()))
        .thenReturn(Collections.singletonList(tag.getId()));
    GiftCertificateWithTagsDto actual = certificateService.update(giftCertificateWithTags, "add");
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test
  public void updateTestUpdateCertificateFieldsAndDeleteExistedTagsReturnsCertificateWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    GiftCertificateWithTagsDto actual = certificateService.update(giftCertificateWithTags, "delete");
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test
  public void updateTestUpdateCertificateFieldsAndDeleteNotExistedTagsReturnsCertificateWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    GiftCertificateWithTagsDto actual = certificateService.update(giftCertificateWithTags, "delete");
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test
  public void findByCriteriaTestWithTagNames() {
    Map<String, String> parameters = new HashMap<>();
    parameters.put("tag", "tag names");
    parameters.put("name", "value");
    parameters.put("description", "description");
    when(certificateMapper.findByCriteria(any(), any(), any())).thenReturn(certificates);
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    List<GiftCertificateWithTagsDto> actual = certificateService.findByCriteria(parameters);
    List<GiftCertificateWithTagsDto> expected = Collections.singletonList(giftCertificateWithTags);
    Assert.assertEquals(expected.size(), actual.size());
    Assert.assertTrue(new ReflectionEquals(actual.get(0)).matches(expected.get(0)));
  }

  @Test
  public void findByCriteriaTestWithoutTagNames() {
    Map<String, String> parameters = new HashMap<>();
    parameters.put("name", "value");
    parameters.put("description", "description");
    when(certificateMapper.findByCriteria(any(), any(), any())).thenReturn(certificates);
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    List<GiftCertificateWithTagsDto> actual = certificateService.findByCriteria(parameters);
    List<GiftCertificateWithTagsDto> expected = Collections.singletonList(giftCertificateWithTags);
    Assert.assertEquals(expected.size(), actual.size());
    Assert.assertTrue(new ReflectionEquals(actual.get(0)).matches(expected.get(0)));
  }

  @Test
  public void findByUserIdTestFoundCertificatesWithTags() {
    when(certificateMapper.selectByUserId(any(), any())).thenReturn(certificates);
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    List<GiftCertificateWithTagsDto> expected = Collections.singletonList(giftCertificateWithTags);
    List<GiftCertificateWithTagsDto> actual = certificateService.findByUserId(1L, new HashMap<>());
    Assert.assertEquals(expected.size(), actual.size());
    Assert.assertTrue(new ReflectionEquals(actual.get(0)).matches(expected.get(0)));
  }

  @Test(expected = ServerException.class)
  public void updatePriceTestNotExistedCertificateThrowsException() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.empty());
    certificateService.updatePrice(1L, BigDecimal.valueOf(10.5));
  }

  @Test
  public void updatePriceTestCorrectPriceUpdate() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    certificateService.updatePrice(1L, BigDecimal.valueOf(10.5));
    GiftCertificateWithTagsDto actual = certificateService.updatePrice(1L, BigDecimal.valueOf(10.5));
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }
}