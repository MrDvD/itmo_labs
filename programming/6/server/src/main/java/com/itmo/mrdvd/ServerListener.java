package com.itmo.mrdvd;

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

import com.itmo.mrdvd.proxy.EmptyQuery;
import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.service.AbstractListener;

public class ServerListener extends AbstractListener<EmptyQuery, String, Response> {
  protected final Map<SelectionKey, ByteBuffer> buffers;
  protected final int bufferSize;
  protected final Charset chars;
  protected boolean isOpen;

  public ServerListener(
      Selector selector,
      Mapper<String, ? extends EmptyQuery> deserialQuery,
      Mapper<? super Response, String> serialResponse) {
    this(selector, deserialQuery, serialResponse, 16384, Charset.forName("UTF-8"));
  }

  public ServerListener(
      Selector selector,
      Mapper<String, ? extends EmptyQuery> deserialQuery,
      Mapper<? super Response, String> serialResponse,
      int bufferSize,
      Charset chars) {
    this(
        selector,
        deserialQuery,
        serialResponse,
        bufferSize,
        chars,
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>());
  }

  public ServerListener(
      Selector selector,
      Mapper<String, ? extends EmptyQuery> deserialQuery,
      Mapper<? super Response, String> serialResponse,
      int bufferSize,
      Charset chars,
      Map<SelectionKey, AbstractSelectableChannel> sockets,
      Map<SelectionKey, ByteBuffer> buffers,
      Map<SelectionKey, Function<Query, Response>> callbacks) {
    super(selector, deserialQuery, serialResponse, sockets, callbacks);
    this.buffers = buffers;
    this.bufferSize = bufferSize;
    this.chars = chars;
  }

  /** Waits for incoming connections in a non-blocking way. */
  @Override
  public void start() throws IllegalStateException, RuntimeException {
    if (this.deserialQuery == null) {
      throw new IllegalStateException("Не предоставлен маппер входящих запросов.");
    }
    if (this.serialResponse == null) {
      throw new IllegalStateException("Не предоставлен маппер исходящих запросов.");
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
                Optional<? extends EmptyQuery> q = this.deserialQuery.convert(receivedData);
                if (q.isPresent()) {
                  Optional<String> serialized = this.serialResponse.convert(this.callbacks.get(key).apply(q.get()));
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
