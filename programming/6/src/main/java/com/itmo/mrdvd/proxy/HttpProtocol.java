package com.itmo.mrdvd.proxy;

import com.itmo.mrdvd.device.Serializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.impl.io.AbstractMessageParser;
import org.apache.hc.core5.http.io.SessionInputBuffer;

public class HttpProtocol implements TransportProtocol {
  protected final AbstractMessageParser<ClassicHttpResponse> parser;
  protected final Function<String, ClassicHttpRequest> newRequest;
  protected final BiFunction<InputStream, ContentType, HttpEntity> newEntity;
  protected final Map<Class<?>, Serializer> serializers;

  public HttpProtocol(
      AbstractMessageParser<ClassicHttpResponse> parser,
      Function<String, ClassicHttpRequest> newRequest,
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
    return serialized.isEmpty() ? Optional.empty() : wrapPayload(url, type, serialized.get());
  }

  @Override
  public Optional<String> wrapPayload(String url, ContentType type, String payload) {
    ClassicHttpRequest request = this.newRequest.apply(url);
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
