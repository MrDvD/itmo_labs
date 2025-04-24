package com.itmo.mrdvd;

import com.itmo.mrdvd.device.Serializer;
import com.itmo.mrdvd.proxy.ClientProxy;
import com.itmo.mrdvd.proxy.TransportProtocol;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Optional;

import org.apache.hc.core5.http.ContentType;

public class CollectionClientProxy implements ClientProxy {
  protected SocketChannel socket;
  protected TransportProtocol protocol;

  public CollectionClientProxy(TransportProtocol proto) {
    this.protocol = proto;
  }

  @Override
  public void setProtocol(TransportProtocol proto) {
    if (proto != null) {
      this.protocol = proto;
    }
  }

  @Override
  public Optional<TransportProtocol> getProtocol() {
    return Optional.of(this.protocol);
  }

  @Override
  public void connect(SocketAddress addr) throws RuntimeException, IOException {
    if (this.socket != null) {
      this.socket.close();
    }
    this.socket = SocketChannel.open();
    this.socket.connect(addr);
  }

  @Override
  public String send(String payload, ContentType content) throws IllegalStateException, RuntimeException {
    try {
      if (this.socket == null) {
        throw new IllegalStateException("Подключение к серверу не установлено.");
      }
      Optional<String> request =
          this.protocol.wrapPayload(this.socket.getRemoteAddress().toString(), payload, content);
      if (request.isEmpty()) {
        throw new RuntimeException("[ERROR] Не удалось сериализовать запрос.");
      }
      ByteBuffer buffer = ByteBuffer.wrap(request.get().getBytes());
      this.socket.write(buffer);
      buffer.clear();
      this.socket.read(buffer);
      String response = new String(buffer.array()).trim();
      buffer.clear();
      return response;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String send(Object payload) throws RuntimeException {
    if (this.protocol.getSerializers().isEmpty()) {
      throw new IllegalStateException("Отсутствует сериализатор для формирования запроса.");
    }
    Serializer serial = this.protocol.getSerializers().get(0);
    Optional<String> result = serial.serialize(payload);
    if (result.isEmpty()) {
      throw new RuntimeException("[ERROR] Не удалось сериализовать переданный объект.");
    }
    return send(result.get(), serial.getContentType());
  }
}
