package com.itmo.mrdvd.device;

import com.fasterxml.jackson.databind.JavaType;
import java.util.Optional;

public interface Deserializer {
  public <T> Optional<? super T> deserialize(String str, Class<?> clz);

  public <T> Optional<? super T> deserialize(String str, JavaType type);
}
