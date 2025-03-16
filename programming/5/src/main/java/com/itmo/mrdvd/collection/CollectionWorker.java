package com.itmo.mrdvd.collection;

import java.util.Optional;

import com.itmo.mrdvd.builder.Builder;

public interface CollectionWorker<T,V> {
  public Optional<T> add(Builder<T> obj);

  public Optional<T> get(Long id);

  public Optional<T> update(Long id, Builder<T> obj);

  public void remove(Long id);

  public void clear();

  public V getCollection();
}
