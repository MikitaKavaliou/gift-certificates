package com.epam.esm.service;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.esm.dto.EntityListDto;
import com.epam.esm.exception.ServerException;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.Tag;
import com.epam.esm.service.impl.TagServiceImpl;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;

@RunWith(MockitoJUnitRunner.class)
public class TagServiceTest {

  private static Tag tag;
  @Mock
  private TagMapper tagMapper;
  @InjectMocks
  private TagServiceImpl tagService;

  @BeforeClass
  public static void beforeClass() {
    tag = new Tag(1L, "1");
  }

  @Test
  public void createTestInsertNewTagReturnsNewTag() {
    Tag actual = tagService.create(tag);
    Assert.assertEquals(tag, actual);
  }

  @Test(expected = ServerException.class)
  public void createTestInsertNewTagThrowsException() {
    doThrow(DataIntegrityViolationException.class).when(tagMapper).insert(any());
    tagService.create(tag);
  }

  @Test
  public void findByIdTestFoundCorrectTag() {
    when(tagMapper.selectById(any())).thenReturn(Optional.of(tag));
    Tag actual = tagService.findById(1L);
    Assert.assertEquals(tag, actual);
  }

  @Test(expected = ServerException.class)
  public void findByIdTestNotFoundThrowsException() {
    when(tagMapper.selectById(any())).thenReturn(Optional.empty());
    tagService.findById(1L);
  }

  @Test
  public void findAllTestCorrectFoundTags() {
    EntityListDto<Tag> expected = new EntityListDto<>(Collections.singletonList(tag));
    when(tagMapper.selectAll(any())).thenReturn(Collections.singletonList(tag));
    EntityListDto<Tag> actual = tagService.findAll(new HashMap<>());
    Assert.assertEquals(expected.getGiftCertificatesWithTags().size(), actual.getGiftCertificatesWithTags().size());
  }

  @Test
  public void findTheMostPopularTagOfHighestSpendingUserTestCorrectFoundTag() {
    when(tagMapper.selectMostPopularTagOfHighestSpendingUser()).thenReturn(tag);
    Assert.assertEquals(tag, tagService.findTheMostPopularTagOfHighestSpendingUser());
  }

  @Test
  public void deleteCorrectMethodCall() {
    long tagId = 1L;
    tagService.delete(tagId);
    verify(tagMapper).deleteById(tagId);
  }
}