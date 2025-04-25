package com.itmo.mrdvd;

import com.itmo.mrdvd.proxy.ServerProxy;
import com.itmo.mrdvd.proxy.TransportProtocol;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
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

import com.itmo.mrdvd.executor.queries.Query;

public class CollectionServerProxy implements ServerProxy {
  protected final Selector selector;
  protected final Map<SelectionKey, AbstractSelectableChannel> sockets;
  protected final Map<SelectionKey, ByteBuffer> buffers;
  protected Function<Query, Query> callback;
  protected boolean isOpen;
  protected final int bufferSize;
  protected final Charset chars;

  protected TransportProtocol protocol;
  protected Optional<InputStreamReader> in;
  protected Optional<OutputStreamWriter> out;

  public CollectionServerProxy(Selector selector, TransportProtocol proto, Function<Query, Query> callback) {
    this(selector, proto, callback, 4096, Charset.forName("UTF-8"));
  }

  public CollectionServerProxy(Selector selector, TransportProtocol proto, Function<Query, Query> callback, int bufferSize, Charset chars) {
    this(selector, proto, callback, bufferSize, chars, new HashMap<>(), new HashMap<>());
  }

  public CollectionServerProxy(
      Selector selector,
      TransportProtocol proto,
      Function<Query, Query> callback,
      int bufferSize,
      Charset chars,
      Map<SelectionKey, AbstractSelectableChannel> sockets,
      Map<SelectionKey, ByteBuffer> buffers) {
    this.selector = selector;
    this.protocol = proto;
    this.bufferSize = bufferSize;
    this.chars = chars;
    this.sockets = sockets;
    this.buffers = buffers;
  }

  @Override
  public void setProtocol(TransportProtocol proto) {
    if (proto != null) {
      this.protocol = proto;
    }
  }

  @Override
  public Optional<TransportProtocol> getProtocol() {
    return Optional.ofNullable(this.protocol);
  }

  @Override
  public void addListener(ServerSocketChannel sock) throws IOException {
    sock.configureBlocking(false);
    this.sockets.put(sock.register(selector, SelectionKey.OP_ACCEPT), sock);
  }

  @Override
  public void setCallback(Function<Query, Query> callback) {
    this.callback = callback;
  }

  /** 
   * Waits for incoming connections in a non-blocking way.
   */
  @Override
  public void listen() throws IOException, IllegalStateException {
    this.isOpen = true;
    while (this.isOpen) {
      this.selector.select();
      Set<SelectionKey> keys = selector.selectedKeys();
      for (SelectionKey key : keys) {
        if (key.isAcceptable()) {
          SocketChannel client = ((ServerSocketChannel) this.sockets.get(key)).accept();
          client.configureBlocking(false);
          SelectionKey clientKey = client.register(selector, SelectionKey.OP_READ);
          this.sockets.put(clientKey, client);
          this.buffers.put(clientKey, ByteBuffer.allocate(bufferSize));
        }
        if (key.isReadable()) {
          SocketChannel client = (SocketChannel) key.channel();
          int chr = client.read(this.buffers.get(key));
          if (chr == -1) {
            Optional<TransportProtocol> proto = getProtocol();
            if (proto.isEmpty() || proto.get().getDeserializers().isEmpty()) {
              client.close();
              throw new IllegalStateException("Не предоставлен протокол для обработки запроса.");
            }
            if (this.callback == null) {
              client.close();
              throw new IllegalStateException("Не предоставлен метод для обработки запроса.");
            }
            Optional<?> rawIn = proto.get().getDeserializers().get(0).deserialize(this.buffers.get(key).toString(), Query.class);
            this.buffers.get(key).clear();
            if (rawIn.isPresent()) {
              Query outQ = this.callback.apply((Query) rawIn.get());
              Optional<String> outSerialized = proto.get().getSerializers().get(0).serialize(outQ);
              if (outSerialized.isPresent()) {
                client.write(this.chars.encode(outSerialized.get()));
              }
            }
            client.close();
          }
        }
      }
    }
  }

  @Override
  public void close() {
    this.isOpen = false;
  }
}
