package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Tag service.
 */
@Service
public class TagServiceImpl implements TagService {

  private final TagDao tagDao;

  /**
   * Instantiates a new Tag service.
   *
   * @param tagDao the tag dao
   */
  @Autowired
  public TagServiceImpl(TagDao tagDao) {
    this.tagDao = tagDao;
  }

  @Override
  public Tag create(Tag tag) {
    return tagDao.findByName(tag.getName())
        .orElseGet(() -> tagDao.findById(tagDao.create(tag))
            .orElseThrow(() -> new ServerException(ExceptionType.ERROR_CREATING_ENTITY)));
  }

  @Override
  public Tag findById(Long id) {
    return tagDao.findById(id).orElseThrow(() -> new ServerException(ExceptionType.RESOURCE_NOT_FOUND));
  }

  @Override
  public List<Tag> findAll() {
    return tagDao.findAll();
  }

  @Override
  public Tag findTheMostPopularTagOfHighestSpendingUser() {
    return tagDao.findTheMostPopularTagOfHighestSpendingUser();
  }

  @Override
  public int delete(Long id) {
    return tagDao.delete(id);
  }
}