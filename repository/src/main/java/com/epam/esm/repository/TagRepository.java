package com.epam.esm.repository;

import com.epam.esm.model.Tag;


/**
 * The interface Tag repository.Interface defines specific methods for working with `tag` database table. *
 */
public interface TagRepository extends GenericRepository<Tag, Long> {

  Long create(Tag tag);
}