package com.itmo.mrdvd.collection;

import java.util.Optional;

public interface CrudWorker<T, V extends java.util.Collection<? extends T>> {
  public Optional<T> add(T obj) throws IllegalArgumentException;

  public Optional<T> get(Long id);

  public V getAll();

  public Optional<T> update(Long id, T obj);

  public void remove(Long id);

  public void clear();
}
