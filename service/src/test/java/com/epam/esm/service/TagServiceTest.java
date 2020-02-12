package com.epam.esm.service;


import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TagServiceTest {
/*
  @Mock
  private TagRepository tagRepository;
  @InjectMocks
  private TagServiceImpl tagService;

  @Test
  public void findByIdFoundCorrectTag() {
    Tag expected = new Tag(1L, "1");
    List<Tag> tags = Collections.singletonList(expected);
    when(tagRepository.query(any(TagIdSpecification.class))).thenReturn(tags);
    Tag actual = tagService.findById(1L);
    Assert.assertEquals(expected, actual);
  }

  @Test(expected = ServerException.class)
  public void findByIdThrowsException() {
    List<Tag> tags = new ArrayList<>();
    when(tagRepository.query(any(TagIdSpecification.class))).thenReturn(tags);
    tagService.findById(1L);
  }

  @Test
  public void findAllFoundCorrectTagList() {
    Tag firstTag = new Tag(1L, "1");
    Tag secondTag = new Tag(2L, "2");
    List<Tag> expected = Arrays.asList(firstTag, secondTag);
    when(tagRepository.query(any(AllTagsSpecification.class))).thenReturn(expected);
    List<Tag> actual = tagService.findAll();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void createCreatesNewTagAndReturningItsId() {
    long expectedTagId = 1;
    Tag tag = new Tag(expectedTagId, "1");
    when(tagRepository.query(any(TagNameSpecification.class))).thenReturn(new ArrayList<>());
    when(tagRepository.create(tag)).thenReturn(expectedTagId);
    long actualTagId = tagService.create(tag);
    Assert.assertEquals(expectedTagId, actualTagId);
  }

  @Test
  public void createCorrectReturnsExistingTagId() {
    long expectedTagId = 1;
    Tag tag = new Tag(expectedTagId, "1");
    List<Tag> tagList = Collections.singletonList(tag);
    when(tagRepository.query(any(TagNameSpecification.class))).thenReturn(tagList);
    long actualTagId = tagService.create(tag);
    Assert.assertEquals(expectedTagId, actualTagId);
  }

  @Test
  public void deleteCorrectMethodCall() {
    long tagId = 1L;
    tagService.delete(tagId);
    verify(tagRepository).delete(1L);
  }*/
}