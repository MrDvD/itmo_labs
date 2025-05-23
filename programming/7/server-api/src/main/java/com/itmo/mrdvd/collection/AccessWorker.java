package com.itmo.mrdvd.collection;

import java.util.Optional;

public interface AccessWorker<T> {
  public Optional<T> get();

  public void set(T obj);
}
