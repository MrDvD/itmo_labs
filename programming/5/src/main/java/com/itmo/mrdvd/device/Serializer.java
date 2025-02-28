package com.itmo.mrdvd.device;

public interface Serializer<T> {
   public String serialize(T obj);
}
