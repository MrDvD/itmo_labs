package com.itmo.mrdvd.device;

import com.fasterxml.jackson.databind.JavaType;
import java.util.Optional;

public interface Deserializer {
  public <T> Optional<? extends T> deserialize(String str, Class<T> clz);

  public <T> Optional<? extends T> deserialize(String str, JavaType type);
}
