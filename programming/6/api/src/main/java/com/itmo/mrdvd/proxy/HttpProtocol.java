package com.itmo.mrdvd.proxy;

import com.itmo.mrdvd.device.Deserializer;
import com.itmo.mrdvd.device.Serializer;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
  protected final List<Serializer> serializers;
  protected final List<Deserializer> deserializers;

  public HttpProtocol(
      AbstractMessageParser<? extends ClassicHttpRequest> parser,
      HttpRequestFactory<? extends ClassicHttpRequest> newRequest) {
    this(parser, newRequest, new ArrayList<>(), new ArrayList<>());
  }

  public HttpProtocol(
      AbstractMessageParser<? extends ClassicHttpRequest> parser,
      HttpRequestFactory<? extends ClassicHttpRequest> newRequest,
      List<Serializer> serializers,
      List<Deserializer> deserializers) {
    this.parser = parser;
    this.newRequest = newRequest;
    this.serializers = serializers;
    this.deserializers = deserializers;
  }

  @Override
  public Optional<String> wrapPayload(String url, Object obj, ContentType type)
      throws RuntimeException {
    if (getSerializers().isEmpty() || getSerializers().get(0) == null) {
      throw new RuntimeException("[ERROR] Отсутствует сериализатор для переданного класса.");
    }
    Optional<String> serialized = getSerializers().get(0).serialize(obj);
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
  public void addSerializationPair(Serializer serial, Deserializer deserial) {
    this.serializers.add(serial);
    this.deserializers.add(deserial);
  }

  @Override
  public List<Serializer> getSerializers() {
    return this.serializers;
  }

  @Override
  public List<Deserializer> getDeserializers() {
    return this.deserializers;
  }
}
