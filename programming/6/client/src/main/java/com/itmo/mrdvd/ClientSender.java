package com.itmo.mrdvd;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.service.AbstractSender;

public class ClientSender extends AbstractSender<Packet> {
  protected final Charset chars;
  protected SocketChannel socket;
  protected SocketAddress addr;

  public ClientSender(
      Mapper<? super Packet, String> serial, Mapper<String, ? extends Packet> deserial) {
    this(serial, deserial, StandardCharsets.UTF_8);
  }

  public ClientSender(
      Mapper<? super Packet, String> serial, Mapper<String, ? extends Packet> deserial, Charset chars) {
    super(serial, deserial);
    this.chars = chars;
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
      ByteBuffer buffer = ByteBuffer.wrap(qSerial.get().getBytes(this.chars));
      this.socket.write(buffer);
      buffer.clear();
      CharBuffer charsBuffer = CharBuffer.allocate(8192);
      CharsetDecoder decoder = this.chars.newDecoder();
      StringBuilder builder = new StringBuilder();
      while (this.socket.read(buffer) != -1 || buffer.position() > 0) {
        buffer.flip();
        decoder.decode(buffer, charsBuffer, true);
        charsBuffer.flip();
        builder.append(charsBuffer);
        charsBuffer.clear();
        buffer.compact();
      }
      String response = builder.toString().trim();
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
