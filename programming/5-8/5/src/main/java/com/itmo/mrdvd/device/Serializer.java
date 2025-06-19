package com.itmo.mrdvd.device;

import java.util.Optional;

public interface Serializer<T> {
  public Optional<String> serialize(T obj);
}
