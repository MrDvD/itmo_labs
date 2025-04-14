package com.itmo.mrdvd.proxy;

import com.itmo.mrdvd.device.Serializer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.function.BiFunction;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpEntityContainer;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpMessage;
import org.apache.hc.core5.http.impl.io.AbstractMessageParser;
import org.apache.hc.core5.http.io.SessionInputBuffer;
import org.apache.hc.core5.http.message.BasicClassicHttpRequest;

public class HttpProtocol<T extends HttpMessage & HttpEntityContainer>
    implements TransportProtocol {
  protected final AbstractMessageParser<T> parser;
  protected final BiFunction<InputStream, ContentType, HttpEntity> newEntity;
  protected final Map<Class, Serializer> serializers;

  public HttpProtocol(
      AbstractMessageParser<T> parser,
      BiFunction<InputStream, ContentType, HttpEntity> newEntity,
      Map<Class, Serializer> serializers) {
    this.parser = parser;
    this.newEntity = newEntity;
    this.serializers = serializers;
  }

  @Override
  public <U> String wrapPayload(String url, U obj, ContentType type) {
    wrapPayload(url, getSerializer(obj.getClass()).serialize(obj), type);
  }

  @Override
  public String wrapPayload(String url, String payload, ContentType type) {
    ClassicHttpRequest request = new BasicClassicHttpRequest("POST", url);
    HttpEntity entity = this.newEntity.apply(new ByteArrayInputStream(payload.getBytes()), type);
    request.setEntity(entity);
    return request.toString();
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
  public <U> void addSerializer(Serializer<U> serial, Class<U> clz) {
    this.serializers.put(clz, serial);
  }
}
