package com.itmo.mrdvd.proxy;

import com.itmo.mrdvd.device.Serializer;
import java.io.InputStream;
import org.apache.hc.core5.http.ContentType;

public interface TransportProtocol {
  public InputStream getPayload(InputStream stream) throws RuntimeException;

  public String wrapPayload(String url, String payload, ContentType type);

  public <T> String wrapPayload(String url, T obj, ContentType type);

  public <T> void addSerializer(Serializer<T> serial, Class<T> clz);

  public <T> Serializer<T> getSerializer(Class<T> clazz);
}
