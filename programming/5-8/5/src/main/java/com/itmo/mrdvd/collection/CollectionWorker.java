package com.itmo.mrdvd.collection;

import com.itmo.mrdvd.builder.builders.Builder;
import com.itmo.mrdvd.builder.updaters.Updater;
import com.itmo.mrdvd.builder.validators.Validator;
import com.itmo.mrdvd.object.HavingId;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

public interface CollectionWorker<T extends HavingId, V> {
  public Optional<T> add(Builder<T> obj) throws IllegalArgumentException;

  public Optional<T> add(Builder<T> obj, Comparator<T> cond, Set<Integer> values)
      throws IllegalArgumentException;

  public Optional<T> add(T rawObject, Validator<T> validator) throws IllegalArgumentException;

  public Optional<T> get(Long id);

  public Optional<T> update(Long id, Updater<T> obj);

  public Optional<T> update(Long id, Updater<T> obj, Comparator<T> cond, Set<Integer> values)
      throws IllegalArgumentException;

  public void remove(Long id);

  public void clear();

  public V getCollection();
}
