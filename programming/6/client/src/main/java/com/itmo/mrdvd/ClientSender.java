package com.itmo.mrdvd;

import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.service.AbstractSender;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Optional;

public class ClientSender extends AbstractSender<Packet> {
  protected SocketChannel socket;
  protected SocketAddress addr;

  public ClientSender(
      Mapper<? super Packet, String> serial, Mapper<String, ? extends Packet> deserial) {
    super(serial, deserial);
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
  public Optional<? extends Packet> send(Packet q) throws IllegalStateException, RuntimeException {
    if (this.socket == null) {
      throw new IllegalStateException("Подключение не установлено для отправки запроса.");
    }
    if (this.serial == null) {
      throw new IllegalStateException("Отсутствует маппер для исходящего запроса.");
    }
    if (this.deserial == null) {
      throw new IllegalStateException("Отсутствует маппер для входящего запроса.");
    }
    try {
      Optional<String> qSerial = this.serial.convert(q);
      if (qSerial.isEmpty()) {
        throw new RuntimeException("Не удалось сериализовать запрос.");
      }
      ByteBuffer buffer = ByteBuffer.wrap(qSerial.get().getBytes());
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
      return this.deserial.convert(response);
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
