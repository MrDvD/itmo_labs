package com.itmo.mrdvd.proxy;

import com.itmo.mrdvd.device.Serializer;
import java.io.InputStream;
import java.util.Optional;
import org.apache.hc.core5.http.ContentType;

public interface TransportProtocol {
  public InputStream getPayload(InputStream stream) throws RuntimeException;

  public Optional<String> wrapPayload(String url, String payload, ContentType type);

  public <T> Optional<String> wrapPayload(String url, T obj, ContentType type);

  public void addSerializer(Serializer serial, Class<?> clz);

  public Serializer getSerializer(Class<?> clazz);
}
