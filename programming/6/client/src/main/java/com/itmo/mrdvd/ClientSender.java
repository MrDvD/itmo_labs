package com.itmo.mrdvd;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Optional;

import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.Mapper;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.service.AbstractSender;

public class ClientSender extends AbstractSender<Query, String, Response> {
  protected SocketChannel socket;

  public ClientSender(Mapper<Query, String> mapper1, Mapper<Response, String> mapper2) {
    super(mapper1, mapper2);
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
  public Optional<Response> send(Query q) throws IllegalStateException, RuntimeException {
    if (this.socket == null) {
      throw new IllegalStateException("Подключение не установлено для отправки запроса.");
    }
    if (this.mapper1 == null) {
      throw new IllegalStateException("Отсутствует маппер для исходящего запроса.");
    }
    if (this.mapper2 == null) {
      throw new IllegalStateException("Отсутствует маппер для входящего запроса.");
    }
    try {
      String qSerial = this.mapper1.wrap(q);
      ByteBuffer buffer = ByteBuffer.wrap(qSerial.getBytes());
      this.socket.write(buffer);
      buffer.clear();
      this.socket.read(buffer);
      String response = new String(buffer.array()).trim();
      buffer.clear();
      return this.mapper2.unwrap(response);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void start() {
    // does nothing?
  }

  @Override
  public void stop() {
    try {
      this.socket.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
