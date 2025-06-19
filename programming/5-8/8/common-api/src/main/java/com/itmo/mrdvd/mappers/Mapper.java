package com.itmo.mrdvd.mappers;

import java.util.Optional;

/** Transforms an DTO to a different type. */
public interface Mapper<T, U> {
  public Optional<U> convert(T obj);
}
