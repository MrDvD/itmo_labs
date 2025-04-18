package com.itmo.mrdvd.device;

import java.util.Optional;

public interface Serializer {

  public <T> Optional<String> serialize(T obj);
}
