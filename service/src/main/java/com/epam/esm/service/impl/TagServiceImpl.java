package com.epam.esm.service.impl;

import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.util.PaginationTool;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Tag service.
 */
@Service
public class TagServiceImpl implements TagService {

  private final TagMapper tagMapper;

  /**
   * Instantiates a new Tag service.
   *
   * @param tagMapper mapper
   */
  @Autowired
  public TagServiceImpl(TagMapper tagMapper) {
    this.tagMapper = tagMapper;
  }

  @Override
  public Tag create(Tag tag) {
    return tagMapper.selectByName(tag.getName())
        .orElseGet(() -> tagMapper.selectById(insertTag(tag))
            .orElseThrow(() -> new ServerException(ExceptionType.ERROR_CREATING_ENTITY)));
  }

  private Long insertTag(Tag tag) {
    tagMapper.insert(tag);
    return tag.getId();
  }

  @Override
  public Tag findById(Long id) {
    return tagMapper.selectById(id).orElseThrow(() -> new ServerException(ExceptionType.RESOURCE_NOT_FOUND));
  }

  @Override
  public List<Tag> findAll(Map<String, String> parameters) {
    return tagMapper.selectAll(PaginationTool.createRowBounds(parameters));
  }

  @Override
  public Tag findTheMostPopularTagOfHighestSpendingUser() {
    return tagMapper.selectMostPopularTagOfHighestSpendingUser();
  }

  @Override
  public int delete(Long id) {
    return tagMapper.deleteById(id);
  }
}