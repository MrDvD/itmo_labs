package com.itmo.mrdvd.proxy;

import com.itmo.mrdvd.device.Deserializer;
import com.itmo.mrdvd.device.Serializer;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequestFactory;
import org.apache.hc.core5.http.MethodNotSupportedException;
import org.apache.hc.core5.http.impl.io.AbstractMessageParser;
import org.apache.hc.core5.http.io.SessionInputBuffer;
import org.apache.hc.core5.http.io.entity.HttpEntities;

public class HttpProtocol implements TransportProtocol {
  protected final AbstractMessageParser<? extends ClassicHttpRequest> parser;
  protected final HttpRequestFactory<? extends ClassicHttpRequest> newRequest;
  protected final Map<Class<?>, Serializer> serializers;
  protected final Map<Class<?>, Deserializer> deserializers;

  public HttpProtocol(
      AbstractMessageParser<? extends ClassicHttpRequest> parser,
      HttpRequestFactory<? extends ClassicHttpRequest> newRequest) {
    this(parser, newRequest, new HashMap<>(), new HashMap<>());
  }

  public HttpProtocol(
      AbstractMessageParser<? extends ClassicHttpRequest> parser,
      HttpRequestFactory<? extends ClassicHttpRequest> newRequest,
      Map<Class<?>, Serializer> serializers,
      Map<Class<?>, Deserializer> deserializers) {
    this.parser = parser;
    this.newRequest = newRequest;
    this.serializers = serializers;
    this.deserializers = deserializers;
  }

  @Override
  public <U> Optional<String> wrapPayload(String url, U obj, ContentType type)
      throws RuntimeException {
    Optional<Serializer> serial = getSerializer(obj.getClass());
    if (serial.isEmpty()) {
      throw new RuntimeException("[ERROR] Отсутствует сериализатор для переданного класса.");
    }
    Optional<String> serialized = serial.get().serialize(obj);
    return serialized.isEmpty() ? Optional.empty() : wrapPayload(url, serialized.get(), type);
  }

  @Override
  public Optional<String> wrapPayload(String url, String payload, ContentType type) {
    ClassicHttpRequest request;
    try {
      request = this.newRequest.newHttpRequest("GET", url);
    } catch (MethodNotSupportedException e) {
      return Optional.empty();
    }
    HttpEntity entity = HttpEntities.create(payload, type);
    request.setEntity(entity);
    return Optional.of(request.toString());
  }

  @Override
  public InputStream getPayload(InputStream stream) throws RuntimeException {
    SessionInputBuffer buffer; // research this buffer as i don't understand why is it used
    try {
      return this.parser.parse(buffer, stream).getEntity().getContent();
    } catch (IOException | HttpException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void addSerializationPair(Class<?> clz, Serializer serial, Deserializer deserial) {
    // in fact, this check is shish, because i cannot check for superclasses and interfaces. only
    // concrete classes...
    this.serializers.put(clz, serial);
    this.deserializers.put(clz, deserial);
  }

  @Override
  public Optional<Serializer> getSerializer(Class<?> clz) {
    return this.serializers.containsKey(clz)
        ? Optional.of(this.serializers.get(clz))
        : Optional.empty();
  }

  @Override
  public Optional<Deserializer> getDeserializer(Class<?> clz) {
    return this.deserializers.containsKey(clz)
        ? Optional.of(this.deserializers.get(clz))
        : Optional.empty();
  }
}
