package com.itmo.mrdvd.device;

import java.util.Optional;

public interface Deserializer<T> {
  public Optional<T> deserialize(String str);
}
