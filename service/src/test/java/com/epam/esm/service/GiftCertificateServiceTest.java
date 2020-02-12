package com.epam.esm.service;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GiftCertificateServiceTest {
/*
  private static final long id = 1L;
  private static GiftCertificate certificate;
  private static List<GiftCertificate> certificates;
  private static Tag tag;
  private static List<Tag> tags;
  private static GiftCertificateWithTags giftCertificateWithTags;

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
    giftCertificateWithTags = new GiftCertificateWithTags(certificate, tags);
  }

  @Test
  public void findByIdFoundCertificate() {
    when(certificateRepository.query(any(CertificateIdSpecification.class)))
        .thenReturn(certificates);
    when(tagRepository.query(any(TagCertificateIdSpecification.class))).thenReturn(tags);
    GiftCertificateWithTags actual = certificateService.findById(1L);
    GiftCertificateWithTags expected = new GiftCertificateWithTags(certificate, tags);
    Assert.assertEquals(expected, actual);
  }

  @Test(expected = ServerException.class)
  public void findByIdThrowsException() {
    when(certificateRepository.query(any(CertificateIdSpecification.class)))
        .thenReturn(new ArrayList<>());
    certificateService.findById(id);
  }

  @Test
  public void createGiftCertificateWithNotNullNotTags() {
    when(tagRepository.query(any(TagNameSpecification.class))).thenReturn(new ArrayList<>());
    when(tagRepository.create(tag)).thenReturn(1L);
    when(certificateRepository.create(any(GiftCertificate.class), anyList()))
        .thenReturn(id);
    long actualId = certificateService.create(giftCertificateWithTags);
    Assert.assertEquals(id, actualId);
  }

  @Test
  public void createGiftCertificateWithNullTags() {
    GiftCertificateWithTags giftCertificateWithTags = new GiftCertificateWithTags(certificate, null);
    when(certificateRepository.create(any(GiftCertificate.class), anyList())).thenReturn(id);
    long actualId = certificateService.create(giftCertificateWithTags);
    Assert.assertEquals(id, actualId);
  }

  @Test
  public void updateGiftCertificateWithNullTagsForCreationAndNullTagsForDeletion() {
    GiftCertificateWithTags giftCertificateWithTags = new GiftCertificateWithTags(certificates.get(0), null);
    when(certificateRepository.update(eq(certificates.get(0)), anyList(), anyList())).thenReturn(id);
    long actualId = certificateService.update(giftCertificateWithTags);
    Assert.assertEquals(id, actualId);
  }

  @Test
  public void updateGiftCertificateWithNotNullTagsForCreationAndNullTagsForDeletion() {
    when(tagRepository.query(any(TagNameSpecification.class))).thenReturn(tags);
    when(certificateRepository.update(any(GiftCertificate.class), anyList(), anyList())).thenReturn(id);
    long actualId = certificateService.update(giftCertificateWithTags);
    Assert.assertEquals(id, actualId);
  }

  @Test
  public void updateGiftCertificateWithExistedTagForCreationAndNullTagsForDeletion() {
    when(tagRepository.query(any(TagNameSpecification.class))).thenReturn(tags);
    when(certificateRepository.update(any(GiftCertificate.class), anyList(), anyList())).thenReturn(id);
    when(tagRepository.query(any(TagIdCertificateIdSpecification.class)))
        .thenReturn(Collections.singletonList(new Tag()));
    long actualId = certificateService.update(giftCertificateWithTags);
    Assert.assertEquals(id, actualId);
  }

  @Test
  public void updateGiftCertificateWithNotNullTagsForCreationAndNotNullTagsForDeletion() {
    when(tagRepository.query(any(TagNameSpecification.class))).thenReturn(new ArrayList<>());
    when(tagRepository.create(tag)).thenReturn(1L);
    when(certificateRepository.update(any(GiftCertificate.class), anyList(), anyList())).thenReturn(id);
    giftCertificateWithTags.setTagsForDeletion(Collections.singletonList(new Tag(1L, "some tag")));
    long actualId = certificateService.update(giftCertificateWithTags);
    giftCertificateWithTags.setTagsForDeletion(null);
    Assert.assertEquals(id, actualId);
  }

  @Test
  public void findCertificatesWithTagsFoundCertificateWithTag() {
    Map<String, String> requestCriteria = new HashMap<>();
    when(certificateRepository.query(any(CertificatesCriteriaSpecification.class)))
        .thenReturn(certificates);
    when(tagRepository.query(any(TagCertificateIdSpecification.class))).thenReturn(tags);
    List<GiftCertificateWithTags> actualList = certificateService.findCertificatesWithTags(requestCriteria);
    GiftCertificateWithTags giftCertificateWithTags = new GiftCertificateWithTags(certificate, tags);
    List<GiftCertificateWithTags> expectedList = Collections.singletonList(giftCertificateWithTags);
    Assert.assertEquals(expectedList, actualList);
  }

  @Test
  public void deleteCorrectMethodCall() {
    long tagId = 1L;
    certificateService.delete(tagId);
    verify(certificateRepository).delete(1L);
  }*/
}