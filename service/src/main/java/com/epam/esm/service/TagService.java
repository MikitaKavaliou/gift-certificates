package com.epam.esm.service;

import com.epam.esm.model.Tag;
import java.util.List;
import java.util.Map;

/**
 * The interface Tag service. Interface defines specific operations with Tags.
 */
public interface TagService {

  Tag create(Tag tag);

  Tag findById(Long id);

  /**
   * Find all list.
   *
   * @return the list of tags
   */
  List<Tag> findAll(Map<String, String> parameters);

  Tag findTheMostPopularTagOfHighestSpendingUser();

  int delete(Long id);
}