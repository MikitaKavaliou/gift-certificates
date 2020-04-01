package com.epam.esm.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.esm.dto.EntityListDto;
import com.epam.esm.dto.GiftCertificateUpdateDto;
import com.epam.esm.dto.GiftCertificateWithTagsDto;
import com.epam.esm.exception.ServerException;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
  private static GiftCertificateUpdateDto giftCertificateUpdateDto;

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
    giftCertificateUpdateDto = new GiftCertificateUpdateDto("name", "description",
        BigDecimal.valueOf(5), 4, tags, tags);
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
    certificateService.update(id, giftCertificateUpdateDto);
  }

  @Test(expected = ServerException.class)
  public void updateTestErrorFindingNotExistedCertificateIdThrowsException() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.empty());
    certificateService.update(id, new GiftCertificateUpdateDto());
  }

  @Test
  public void updateTestUpdateNoFieldsForUpdateCertificateWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    GiftCertificateUpdateDto certificate = new GiftCertificateUpdateDto();
    GiftCertificateWithTagsDto actual = certificateService.update(id, certificate);
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test
  public void updateTestUpdateNameReturnsCertificateWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    GiftCertificateUpdateDto certificate = new GiftCertificateUpdateDto();
    certificate.setName("name");
    GiftCertificateWithTagsDto actual = certificateService.update(id, certificate);
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test
  public void updateTestUpdateDescriptionReturnsCertificateWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    GiftCertificateUpdateDto certificate = new GiftCertificateUpdateDto();
    certificate.setDescription("description");
    GiftCertificateWithTagsDto actual = certificateService.update(id, certificate);
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test
  public void updateTestUpdateDurationReturnsCertificateWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    GiftCertificateUpdateDto certificate = new GiftCertificateUpdateDto();
    certificate.setDuration(4);
    GiftCertificateWithTagsDto actual = certificateService.update(id, certificate);
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test
  public void updateTestUpdatePriceReturnsCertificateWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    GiftCertificateUpdateDto certificate = new GiftCertificateUpdateDto();
    certificate.setPrice(BigDecimal.valueOf(10));
    GiftCertificateWithTagsDto actual = certificateService.update(id, certificate);
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test
  public void updateTestUpdateEmptyTagsForUpdateCertificateWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    GiftCertificateUpdateDto certificate = new GiftCertificateUpdateDto();
    certificate.setTagsForDeletion(new ArrayList<>());
    certificate.setTagsForAdding(new ArrayList<>());
    GiftCertificateWithTagsDto actual = certificateService.update(id, certificate);
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test
  public void updateTestUpdateReceivedExistedTagsForUpdateCertificateWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    when(tagMapper.selectTagIdListByTagIdSetAndCertificateId(any(), any())).thenReturn(Collections.singletonList(id));
    GiftCertificateWithTagsDto actual = certificateService.update(id, giftCertificateUpdateDto);
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test
  public void updateTestUpdateReceivedNewTagsForUpdateCertificateWithTags() {
    when(certificateMapper.selectById(any())).thenReturn(Optional.of(certificate));
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    when(tagMapper.selectTagIdListByTagIdSetAndCertificateId(any(), any())).thenReturn(new ArrayList<>());
    GiftCertificateWithTagsDto actual = certificateService.update(id, giftCertificateUpdateDto);
    Assert.assertTrue(new ReflectionEquals(actual).matches(giftCertificateWithTags));
  }

  @Test
  public void findByCriteriaTestWithTagNamesWithCorrectMaxPriceAndIncorrectMinPirce() {
    Map<String, String> parameters = new HashMap<>();
    List<String> tagList = Arrays.asList("tag", "names");
    parameters.put("name", "value");
    parameters.put("description", "description");
    parameters.put("maxPrice", "20");
    parameters.put("minPrice", "asf");
    when(certificateMapper.selectByCriteria(any(), any(), any())).thenReturn(certificates);
    when(certificateMapper.getCountOfSuitableRecordsOfFindByCriteria(any(), any())).thenReturn(1);
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    EntityListDto<GiftCertificateWithTagsDto> actual = certificateService.findByCriteria(parameters, tagList);
    EntityListDto<GiftCertificateWithTagsDto> expected =
        new EntityListDto<>(Collections.singletonList(giftCertificateWithTags), 1);
    Assert.assertEquals(expected.getPagesCount(), actual.getPagesCount());
    Assert.assertEquals(expected.getGiftCertificatesWithTags().size(), actual.getGiftCertificatesWithTags().size());
    Assert.assertTrue(new ReflectionEquals(actual.getGiftCertificatesWithTags().get(0))
        .matches(expected.getGiftCertificatesWithTags().get(0)));

  }

  @Test
  public void findByCriteriaTestWithoutTagNames() {
    Map<String, String> parameters = new HashMap<>();
    List<String> tagList = Arrays.asList("tag", "names");
    parameters.put("name", "value");
    parameters.put("description", "description");
    when(certificateMapper.selectByCriteria(any(), any(), any())).thenReturn(certificates);
    when(certificateMapper.getCountOfSuitableRecordsOfFindByCriteria(any(), any())).thenReturn(1);
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    EntityListDto<GiftCertificateWithTagsDto> actual = certificateService.findByCriteria(parameters, tagList);
    EntityListDto<GiftCertificateWithTagsDto> expected =
        new EntityListDto<>(Collections.singletonList(giftCertificateWithTags), 1);
    Assert.assertEquals(expected.getPagesCount(), actual.getPagesCount());
    Assert.assertEquals(expected.getGiftCertificatesWithTags().size(), actual.getGiftCertificatesWithTags().size());
    Assert.assertTrue(new ReflectionEquals(actual.getGiftCertificatesWithTags().get(0))
        .matches(expected.getGiftCertificatesWithTags().get(0)));
  }

  @Test
  public void findByUserIdTestFoundCertificatesWithTags() {
    when(certificateMapper.selectByUserId(any(), any())).thenReturn(certificates);
    when(tagMapper.selectByCertificateId(any())).thenReturn(tags);
    EntityListDto<GiftCertificateWithTagsDto> expected =
        new EntityListDto<>(Collections.singletonList(giftCertificateWithTags));
    EntityListDto<GiftCertificateWithTagsDto> actual = certificateService.findByUserId(1L, new HashMap<>());
    Assert.assertEquals(expected.getGiftCertificatesWithTags().size(), actual.getGiftCertificatesWithTags().size());
    Assert.assertTrue(new ReflectionEquals(actual.getGiftCertificatesWithTags().get(0))
        .matches(expected.getGiftCertificatesWithTags().get(0)));
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