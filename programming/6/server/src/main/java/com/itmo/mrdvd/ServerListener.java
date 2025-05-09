package com.itmo.mrdvd;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.service.AbstractListener;

public class ServerListener extends AbstractListener<Packet> {
  protected final int bufferSize;
  protected final Charset chars;

  public ServerListener(
      Selector selector,
      Mapper<String, ? extends Packet> deserialPacket,
      Mapper<? super Packet, String> serialPacket) {
    this(selector, deserialPacket, serialPacket, 8192, StandardCharsets.UTF_8);
  }

  public ServerListener(
      Selector selector,
      Mapper<String, ? extends Packet> deserialPacket,
      Mapper<? super Packet, String> serialPacket,
      int bufferSize,
      Charset chars) {
    this(
        selector,
        deserialPacket,
        serialPacket,
        bufferSize,
        chars,
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>());
  }

  public ServerListener(
      Selector selector,
      Mapper<String, ? extends Packet> deserialPacket,
      Mapper<? super Packet, String> serialPacket,
      int bufferSize,
      Charset chars,
      Map<SelectionKey, AbstractSelectableChannel> sockets,
      Map<SelectionKey, ByteBuffer> buffers,
      Map<SelectionKey, Function<Packet, Packet>> callbacks) {
    super(selector, deserialPacket, serialPacket, sockets, buffers, callbacks);
    this.bufferSize = bufferSize;
    this.chars = chars;
  }

  @Override
  public void acceptConnection(SelectionKey key) throws IOException {
    SocketChannel client = ((ServerSocketChannel) this.sockets.get(key)).accept();
    if (client != null) {
      client.configureBlocking(false);
      SelectionKey clientKey = client.register(selector, SelectionKey.OP_READ);
      clientKey.attach(callbacks.get(key));
      buffers.put(clientKey, ByteBuffer.allocate(bufferSize));
      System.out.println("Accepted connection from " + client.getRemoteAddress());
    }
  }

  @Override
  public void handleConnection(SelectionKey key, ByteBuffer buffer) throws IOException {
    try (SocketChannel client = (SocketChannel) key.channel()) {
      int bytesRead = client.read(buffer);
      if (bytesRead > 0) {
        buffer.flip();
        String receivedData = this.chars.decode(buffer).toString();
        buffer.clear();
        Optional<? extends Packet> packet = this.deserialPacket.convert(receivedData);
        if (packet.isPresent()) {
          Function<Packet, Packet> callback = (Function) key.attachment();
          Packet responsePacket = callback.apply(packet.get());
          Optional<String> serialized = this.serialPacket.convert(responsePacket);
          if (serialized.isPresent()) {
            sendResponse(client, serialized.get());
          }
        }
      }
    }
  }

  @Override
  public void sendResponse(SocketChannel client, String response) throws IOException {
    client.write(this.chars.encode(CharBuffer.wrap(response)));
  }
}
