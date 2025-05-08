package com.itmo.mrdvd;

import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.service.AbstractListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class ServerListener extends AbstractListener<Packet> {
  protected final Map<SelectionKey, ByteBuffer> buffers;
  protected final int bufferSize;
  protected final Charset chars;
  protected boolean isOpen;

  public ServerListener(
      Selector selector,
      Mapper<String, ? extends Packet> deserialPacket,
      Mapper<? super Packet, String> serialPacket) {
    this(selector, deserialPacket, serialPacket, 16384, Charset.forName("UTF-8"));
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
    super(selector, deserialPacket, serialPacket, sockets, callbacks);
    this.buffers = buffers;
    this.bufferSize = bufferSize;
    this.chars = chars;
  }

  /** Waits for incoming connections in a non-blocking way. */
  @Override
  public void start() throws IllegalStateException, RuntimeException {
    if (this.deserialPacket == null) {
      throw new IllegalStateException("Не предоставлен маппер входящих пакетов.");
    }
    if (this.serialPacket == null) {
      throw new IllegalStateException("Не предоставлен маппер исходящих пакетов.");
    }
    this.isOpen = true;
    try {
      while (this.isOpen) {
        this.selector.select();
        Set<SelectionKey> keys = selector.selectedKeys();
        for (SelectionKey key : keys) {
          if (key.isAcceptable()) {
            SocketChannel client = ((ServerSocketChannel) this.sockets.get(key)).accept();
            if (client != null) {
              System.out.println("Accepted connection from " + client.getRemoteAddress());
              client.configureBlocking(false);
              SelectionKey clientKey = client.register(selector, SelectionKey.OP_READ);
              this.sockets.put(clientKey, client);
              this.buffers.put(clientKey, ByteBuffer.allocate(bufferSize));
              this.callbacks.put(clientKey, this.callbacks.get(key));
            }
          }
          if (key.isReadable()) {
            try (SocketChannel client = (SocketChannel) key.channel()) {
              ByteBuffer buffer = this.buffers.get(key);
              int bytesRead = client.read(buffer);
              if (bytesRead > 0) {
                buffer.flip();
                String receivedData = this.chars.decode(buffer).toString();
                buffer.clear();
                Optional<? extends Packet> q = this.deserialPacket.convert(receivedData);
                if (q.isPresent()) {
                  Optional<String> serialized =
                      this.serialPacket.convert(this.callbacks.get(key).apply(q.get()));
                  if (serialized.isPresent()) {
                    ByteBuffer responseBuffer = this.chars.encode(serialized.get());
                    while (responseBuffer.hasRemaining()) {
                      client.write(responseBuffer);
                    }
                  }
                }
              }
            }
            this.sockets.remove(key);
            this.buffers.remove(key);
            this.callbacks.remove(key);
          }
          keys.remove(key);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void stop() {
    this.isOpen = false;
  }
}
