package com.itmo.mrdvd.proxy;

import java.io.InputStream;
import java.util.Optional;

import org.apache.hc.core5.http.ContentType;

import com.itmo.mrdvd.device.Serializer;

public interface TransportProtocol {
  public InputStream getPayload(InputStream stream) throws RuntimeException;

  public Optional<String> wrapPayload(String url, ContentType type, String payload);

  public <T> Optional<String> wrapPayload(String url, T obj, ContentType type);

  public void addSerializer(Serializer serial, Class<?> clz);

  public Serializer getSerializer(Class<?> clazz);
}
