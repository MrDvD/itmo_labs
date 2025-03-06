package com.itmo.mrdvd.collection;

import java.util.Optional;

import com.itmo.mrdvd.object.Ticket;

public interface CollectionWorker<T> {
  public Optional<Ticket> add(T obj);

  public Optional<Ticket> get(Long id);

  public Optional<Ticket> update(Long id, T obj);

  public void remove(Long id);

  public void clear();
}
