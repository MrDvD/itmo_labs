package com.itmo.mrdvd.collection;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

import com.itmo.mrdvd.builder.validators.Validator;

public interface CollectionWorker<T extends HavingId, V> {
  public Optional<T> add(T obj, Validator<T> validator) throws IllegalArgumentException;

  public Optional<T> add(T obj, Validator<T> validator, Comparator<T> cond, Set<Integer> values)
      throws IllegalArgumentException;

  public Optional<T> get(Long id);

  public Optional<T> update(Long id, T obj, Validator<T> validator);

  public Optional<T> update(Long id, T obj, Validator<T> validator, Comparator<T> cond, Set<Integer> values)
      throws IllegalArgumentException;

  public void remove(Long id);

  public void clear();

  public V getCollection();
}
