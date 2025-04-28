package com.itmo.mrdvd.proxy;

import java.util.Optional;

/**
 * Transforms an DTO to a different type and vice versa.
 */
public interface Mapper<T, U> {
  public U wrap(T obj);

  public Optional<T> unwrap(U obj);
}
