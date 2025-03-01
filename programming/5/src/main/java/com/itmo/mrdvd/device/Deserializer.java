package com.itmo.mrdvd.device;

public interface Deserializer<T> {
  public T deserialize(String str);
}
