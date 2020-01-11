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
   * Create entity in database returns k.
   *
   * @param entity the entity for creation
   * @return the k id of created entity
   */
  K create(T entity);

  /**
   * Delete.
   *
   * @param id the id of deleting entity
   */
  void delete(K id);

  /**
   * Query list.
   *
   * @param specification the specification of database query with parameters
   * @return the list of found entities
   */
  List<T> query(Specification specification);
}