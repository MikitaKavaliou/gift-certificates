package com.epam.esm.service;

import com.epam.esm.dto.EntityListDto;
import com.epam.esm.model.Tag;
import java.util.Map;

/**
 * The interface Tag service. Interface defines specific operations with Tags.
 */
public interface TagService {

  /**
   * Create tag, returns created tag.
   *
   * @param tag the tag
   * @return created tag
   */
  Tag create(Tag tag);

  /**
   * Find by id tag, returns found tag.
   *
   * @param id the tag id
   * @return found tag
   */
  Tag findById(Long id);

  /**
   * Find all list, returns tag found tags.
   *
   * @param parameters the parameters
   * @return the list of tags
   */
  EntityListDto<Tag> findAll(Map<String, String> parameters);

  /**
   * Find the most popular tag of highest spending user tag.
   *
   * @return the tag
   */
  Tag findTheMostPopularTagOfHighestSpendingUser();

  /**
   * Delete int, returns number of delete rows.
   *
   * @param id the tag id
   * @return the int number of deleted rows.
   */
  int delete(Long id);
}