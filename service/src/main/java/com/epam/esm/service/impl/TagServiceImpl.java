package com.epam.esm.service.impl;

import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.util.PaginationUtil;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    try {
      tagMapper.insert(tag);
      return tag;
    } catch (DataIntegrityViolationException e) {
      throw new ServerException(ExceptionType.INCORRECT_INPUT_DATA);
    }
  }

  @Override
  public Tag findById(Long id) {
    return tagMapper.selectById(id).orElseThrow(() -> new ServerException(ExceptionType.RESOURCE_NOT_FOUND));
  }

  @Override
  public List<Tag> findAll(Map<String, String> parameters) {
    return tagMapper.selectAll(PaginationUtil.createRowBounds(parameters));
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