package com.itmo.mrdvd.device;

import java.util.Optional;

import org.apache.hc.core5.http.ContentType;

public interface Serializer {
  public <T> Optional<String> serialize(T obj);

  public ContentType getContentType();
}
