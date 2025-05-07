package com.itmo.mrdvd.collection;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

import com.itmo.mrdvd.validators.Validator;

public interface CollectionWorker<T extends HavingId, V> {
  /** Adds an object to the collection. */
  public Optional<T> add(T obj) throws IllegalArgumentException;

  /** Adds an object to the collection if it is valid. */
  public Optional<T> add(T obj, Validator<T> validator) throws IllegalArgumentException;

  /** Adds an object to the collection if the passed condition is met for the whole collection. */
  public Optional<T> add(T obj, Comparator<T> cond, Set<Integer> values)
      throws IllegalArgumentException;

  /**
   * Adds an object to the collection if it is valid & the passed condition is met for the whole
   * collection.
   */
  public Optional<T> add(T obj, Validator<T> validator, Comparator<T> cond, Set<Integer> values)
      throws IllegalArgumentException;

  public Optional<T> get(Long id);

  /** Updates an existing object if it is valid. */
  public Optional<T> update(Long id, T obj, Validator<T> validator);

  /** Updates an existing object if it is valid & the passed condition is met for the old object. */
  public Optional<T> update(
      Long id, T obj, Validator<T> validator, Comparator<T> cond, Set<Integer> values)
      throws IllegalArgumentException;

  public void remove(Long id);

  public void clear();

  public V getCollection();
}
