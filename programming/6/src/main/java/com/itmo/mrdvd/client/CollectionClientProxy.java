package com.itmo.mrdvd.client;

import com.itmo.mrdvd.device.Serializer;
import com.itmo.mrdvd.proxy.ClientProxy;
import com.itmo.mrdvd.proxy.TransportProtocol;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Optional;
import org.apache.hc.core5.http.ContentType;

public class CollectionClientProxy implements ClientProxy {
  protected final SocketChannel socket;
  protected TransportProtocol protocol;

  public CollectionClientProxy(SocketChannel socket, TransportProtocol proto) {
    this.socket = socket;
    this.protocol = proto;
  }

  @Override
  public void setProtocol(TransportProtocol proto) {
    if (proto != null) {
      this.protocol = proto;
    }
  }

  @Override
  public String send(String payload, ContentType content) throws RuntimeException {
    try {
      if (this.socket == null) {
        throw new RuntimeException("[ERROR] Передан null-сокет.");
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
    Serializer serial;
    try {
      serial = this.protocol.getSerializers().get(0);
    } catch (IndexOutOfBoundsException e) {
      throw new RuntimeException("[ERROR] Отсутствует какой-либо сериализатор.");
    }
    Optional<String> result = serial.serialize(payload);
    if (result.isEmpty()) {
      throw new RuntimeException("[ERROR] Не удалось сериализовать переданный объект.");
    }
    return send(result.get(), serial.getContentType());
  }
}
