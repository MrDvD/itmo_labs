package com.itmo.mrdvd.collection;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

import com.itmo.mrdvd.builder.Builder;
import com.itmo.mrdvd.builder.Updater;

public interface CollectionWorker<T extends HavingId,V> {
  public Optional<T> add(Builder<T> obj);

  public Optional<T> add(Builder<T> obj, Comparator<T> cond, Set<Integer> values) throws IllegalArgumentException;

  public Optional<T> get(Long id);

  public Optional<T> update(Long id, Updater<T> obj);

  public Optional<T> update(Long id, Updater<T> obj, Comparator<T> cond, Set<Integer> values) throws IllegalArgumentException;

  public void remove(Long id);

  public void clear();

  public V getCollection();
}
