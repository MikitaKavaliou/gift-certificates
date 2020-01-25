package com.epam.esm.service;

import com.epam.esm.model.Tag;
import java.util.List;

/**
 * The interface Tag service. Interface defines specific operations with Tags.
 */
public interface TagService extends Service<Tag, Long> {

  /**
   * Find all list.
   *
   * @return the list of tags
   */
  List<Tag> findAll();
}