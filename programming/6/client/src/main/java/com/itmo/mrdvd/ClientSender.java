package com.itmo.mrdvd;

import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.service.AbstractSender;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Optional;

public class ClientSender extends AbstractSender<Query, String, Response> {
  protected SocketChannel socket;
  protected SocketAddress addr;

  public ClientSender(
      Mapper<? super Query, String> mapper1, Mapper<? extends Response, String> mapper2) {
    super(mapper1, mapper2);
  }

  @Override
  public void setAddress(SocketAddress addr) {
    this.addr = addr;
  }

  @Override
  public void connect() throws IllegalStateException, IOException {
    if (this.addr == null) {
      throw new IllegalStateException("Адрес не установлен для подключения.");
    }
    if (this.socket != null) {
      this.socket.close();
    }
    this.socket = SocketChannel.open();
    this.socket.connect(this.addr);
  }

  @Override
  public Optional<? extends Response> send(Query q) throws IllegalStateException, RuntimeException {
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
      StringBuilder responseBuilder = new StringBuilder();
      int bytesRead;
      while ((bytesRead = this.socket.read(buffer)) > 0) {
        buffer.flip();
        responseBuilder.append(new String(buffer.array(), 0, bytesRead));
        buffer.clear();
      }
      String response = responseBuilder.toString().trim();
      // System.out.println(response);
      return this.mapper2.unwrap(response);
    } catch (IOException | RuntimeException e) {
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
