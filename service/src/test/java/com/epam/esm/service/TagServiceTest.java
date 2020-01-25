package com.epam.esm.service;


import com.epam.esm.exception.ServerException;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.specification.impl.tag.AllTagsSpecification;
import com.epam.esm.repository.specification.impl.tag.TagIdSpecification;
import com.epam.esm.repository.specification.impl.tag.TagNameSpecification;
import com.epam.esm.service.impl.TagServiceImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TagServiceTest {

  @Mock
  private TagRepository tagRepository;
  @InjectMocks
  private TagServiceImpl tagService;

  @Test
  public void findByIdFoundCorrectTag() {
    Tag expected = new Tag(1L, "1");
    List<Tag> tags = Collections.singletonList(expected);
    Mockito.when(tagRepository.query(Mockito.any(TagIdSpecification.class))).thenReturn(tags);
    Tag actual = tagService.findById(1L);
    Assert.assertEquals(expected, actual);
  }

  @Test(expected = ServerException.class)
  public void findByIdThrowsException() {
    List<Tag> tags = new ArrayList<>();
    Mockito.when(tagRepository.query(Mockito.any(TagIdSpecification.class))).thenReturn(tags);
    tagService.findById(1L);
  }

  @Test
  public void findAllFoundCorrectTagList() {
    Tag firstTag = new Tag(1L, "1");
    Tag secondTag = new Tag(2L, "2");
    List<Tag> expected = Arrays.asList(firstTag, secondTag);
    Mockito.when(tagRepository.query(Mockito.any(AllTagsSpecification.class))).thenReturn(expected);
    List<Tag> actual = tagService.findAll();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void createCreatesNewTagAndReturningItsId() {
    long expectedTagId = 1;
    Tag tag = new Tag(expectedTagId, "1");
    Mockito.when(tagRepository.query(Mockito.any(TagNameSpecification.class))).thenReturn(new ArrayList<>());
    Mockito.when(tagRepository.create(tag)).thenReturn(expectedTagId);
    long actualTagId = tagService.create(tag);
    Assert.assertEquals(expectedTagId, actualTagId);
  }

  @Test
  public void createCorrectReturnsExistingTagId() {
    long expectedTagId = 1;
    Tag tag = new Tag(expectedTagId, "1");
    List<Tag> tagList = Collections.singletonList(tag);
    Mockito.when(tagRepository.query(Mockito.any(TagNameSpecification.class))).thenReturn(tagList);
    long actualTagId = tagService.create(tag);
    Assert.assertEquals(expectedTagId, actualTagId);
  }

  @Test
  public void deleteCorrectMethodCall() {
    long tagId = 1L;
    tagService.delete(tagId);
    Mockito.verify(tagRepository).delete(1L);
  }
}