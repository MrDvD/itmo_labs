package com.itmo.mrdvd.collection;

import java.util.Optional;
import java.util.function.Predicate;

public interface CrudWorker<T, V extends java.util.Collection<? extends T>, K> {
  /** Adds an object if some condition is met. */
  public Optional<T> add(T obj, Predicate<T> cond) throws IllegalArgumentException;

  public Optional<? extends T> get(K id);

  public V getAll();

  /** Updates an object if some condition is met. */
  public Optional<T> update(K id, T obj, Predicate<T> cond);

  public void remove(K id);

  public void clear();
}
