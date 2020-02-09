package com.epam.esm.repository;

import com.epam.esm.repository.specification.Specification;
import java.util.List;

/**
 * The interface Generic repository. Interface defines common methods for working with the database tables.
 *
 * @param <T> the entity type parameter
 * @param <K> the entity key(id) parameter
 */
public interface GenericRepository<T, K> {

  /**
   * Delete.
   *
   * @param id the id of deleting entity
   * @return the number of deleted rows
   */
  int delete(K id);

  /**
   * Query list.
   *
   * @param specification the specification of database query with parameters
   * @return the list of found entities
   */
  List<T> query(Specification specification);
}