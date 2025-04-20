package com.itmo.mrdvd.device;

import java.util.Optional;

public interface Deserializer {
  public <T> Optional<? super T> deserialize(String str, Class<?> clz);
}
