package com.itmo.mrdvd.client;

import com.itmo.mrdvd.device.Serializer;
import com.itmo.mrdvd.proxy.ClientProxy;
import com.itmo.mrdvd.proxy.TransportProtocol;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Optional;
import org.apache.hc.core5.http.ContentType;

public class CollectionClientProxy implements ClientProxy {
  protected final Socket socket;
  protected TransportProtocol protocol;
  protected Optional<InputStreamReader> in;
  protected Optional<OutputStreamWriter> out;

  public CollectionClientProxy(Socket sock, TransportProtocol proto) {
    this.socket = sock;
    this.protocol = proto;
    try {
      this.in = Optional.of(new InputStreamReader(sock.getInputStream()));
      this.out = Optional.of(new OutputStreamWriter(sock.getOutputStream()));
    } catch (IOException e) {
      this.in = Optional.empty();
      this.out = Optional.empty();
    }
  }

  @Override
  public void setProtocol(TransportProtocol proto) {
    if (proto != null) {
      this.protocol = proto;
    }
  }

  @Override
  public void send(String payload, ContentType content) throws RuntimeException {
    if (this.out.isEmpty()) {
      throw new RuntimeException("Ошибка записи в TCP-сокет.");
    }
    try {
      Optional<String> request =
          this.protocol.wrapPayload(this.socket.getInetAddress().toString(), payload, content);
      if (request.isEmpty()) {
        throw new RuntimeException("Ошибка сериализации запроса.");
      }
      this.out.get().write(request.get());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void send(Object payload) throws RuntimeException {
    Serializer serial = this.protocol.getSerializer(payload.getClass());
    Optional<String> result = serial.serialize(payload);
    if (result.isEmpty()) {
      throw new RuntimeException("Ошибка сериализации объекта.");
    }
    send(result.get(), serial.getContentType());
  }
}
