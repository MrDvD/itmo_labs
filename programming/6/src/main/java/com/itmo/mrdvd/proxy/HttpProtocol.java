package com.itmo.mrdvd.proxy;

import com.itmo.mrdvd.device.Serializer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequestFactory;
import org.apache.hc.core5.http.MethodNotSupportedException;
import org.apache.hc.core5.http.impl.io.AbstractMessageParser;
import org.apache.hc.core5.http.io.SessionInputBuffer;

public class HttpProtocol implements TransportProtocol {
  protected final AbstractMessageParser<? extends ClassicHttpRequest> parser;
  protected final HttpRequestFactory<? extends ClassicHttpRequest> newRequest;
  protected final BiFunction<InputStream, ContentType, HttpEntity> newEntity;
  protected final Map<Class<?>, Serializer> serializers;

  public HttpProtocol(
      AbstractMessageParser<? extends ClassicHttpRequest> parser,
      HttpRequestFactory<? extends ClassicHttpRequest> newRequest,
      BiFunction<InputStream, ContentType, HttpEntity> newEntity) {
    this(parser, newRequest, newEntity, new HashMap<>());
  }

  public HttpProtocol(
      AbstractMessageParser<? extends ClassicHttpRequest> parser,
      HttpRequestFactory<? extends ClassicHttpRequest> newRequest,
      BiFunction<InputStream, ContentType, HttpEntity> newEntity,
      Map<Class<?>, Serializer> serializers) {
    this.parser = parser;
    this.newRequest = newRequest;
    this.newEntity = newEntity;
    this.serializers = serializers;
  }

  @Override
  public <U> Optional<String> wrapPayload(String url, U obj, ContentType type) {
    Optional<String> serialized = getSerializer(obj.getClass()).serialize(obj);
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
    HttpEntity entity = this.newEntity.apply(new ByteArrayInputStream(payload.getBytes()), type);
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
  public void addSerializer(Serializer serial, Class<?> clz) {
    this.serializers.put(clz, serial);
  }

  @Override
  public Serializer getSerializer(Class<?> clz) {
    return this.serializers.get(clz);
  }
}
