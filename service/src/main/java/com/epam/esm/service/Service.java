package com.epam.esm.service;

/**
 * The interface Service. Common interface defines set of operations with objects.
 *
 * @param <T> the entity type parameter
 * @param <K> the entity key type parameter
 */
public interface Service<T, K> {

  /**
   * Create entity returns k.
   *
   * @param object the object
   * @return the k id of created entity
   */
  K create(T object);

  /**
   * Delete entity by id.
   *
   * @param id the id of deleting
   */
  void delete(K id);

  /**
   * Find entity by id returns entity t.
   *
   * @param id the id of entity
   * @return the t entity type
   */
  T findById(K id);
}