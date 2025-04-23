package com.itmo.mrdvd.proxy;

import com.itmo.mrdvd.device.Deserializer;
import com.itmo.mrdvd.device.Serializer;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import org.apache.hc.core5.http.ContentType;

public interface TransportProtocol {
  public InputStream getPayload(InputStream stream) throws RuntimeException;

  public Optional<String> wrapPayload(String url, String payload, ContentType type);

  public <T> Optional<String> wrapPayload(String url, T obj, ContentType type)
      throws RuntimeException;

  public void addSerializationPair(Serializer serial, Deserializer deserial);

  public List<Serializer> getSerializers();

  public List<Deserializer> getDeserializers();
}
