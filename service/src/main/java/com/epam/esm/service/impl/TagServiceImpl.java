package com.epam.esm.service.impl;

import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.specification.impl.tag.AllTagsSpecification;
import com.epam.esm.repository.specification.impl.tag.TagIdSpecification;
import com.epam.esm.repository.specification.impl.tag.TagNameSpecification;
import com.epam.esm.service.TagService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Tag service.
 */
@Service
public class TagServiceImpl implements TagService {

  private TagRepository tagRepository;

  /**
   * Instantiates a new Tag service.
   *
   * @param tagRepository the tag repository
   */
  @Autowired
  public TagServiceImpl(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  @Override
  public Long create(Tag tag) {
    List<Tag> tags = tagRepository.query(new TagNameSpecification(tag.getName()));
    return tags.isEmpty() ? tagRepository.create(tag) : tags.get(0).getId();
  }

  @Override
  public Tag findById(Long id) {
    List<Tag> tags = tagRepository.query(new TagIdSpecification(id));
    if (tags.isEmpty()) {
      throw new ServerException(ExceptionType.RESOURCE_NOT_FOUND);
    }
    return tags.get(0);
  }

  @Override
  public List<Tag> findAll() {
    return tagRepository.query(new AllTagsSpecification());
  }

  @Override
  public int delete(Long id) {
    return tagRepository.delete(id);
  }
}