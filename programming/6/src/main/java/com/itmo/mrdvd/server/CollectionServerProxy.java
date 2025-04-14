package com.itmo.mrdvd.server;

import com.itmo.mrdvd.proxy.ServerProxy;
import com.itmo.mrdvd.proxy.TransportProtocol;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CollectionServerProxy implements ServerProxy {
  protected final Selector selector;
  protected final Map<SelectionKey, AbstractSelectableChannel> sockets;
  protected final Map<SelectionKey, ByteBuffer> buffers;
  protected boolean isOpen;
  protected int bufferSize;

  protected TransportProtocol protocol;
  protected Optional<InputStreamReader> in;
  protected Optional<OutputStreamWriter> out;

  public CollectionServerProxy(Selector selector, TransportProtocol proto) {
    this(selector, proto, 4096);
  }

  public CollectionServerProxy(Selector selector, TransportProtocol proto, int bufferSize) {
    this(selector, proto, bufferSize, new HashMap<>(), new HashMap<>());
  }

  public CollectionServerProxy(
      Selector selector,
      TransportProtocol proto,
      int bufferSize,
      Map<SelectionKey, AbstractSelectableChannel> sockets,
      Map<SelectionKey, ByteBuffer> buffers) {
    this.selector = selector;
    this.protocol = proto;
    this.bufferSize = bufferSize;
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
  public void addListener(ServerSocketChannel sock) throws IOException {
    sock.configureBlocking(false);
    this.sockets.put(sock.register(selector, SelectionKey.OP_ACCEPT), sock);
  }

  /** Waits for incoming connections in a non-blocking way. */
  @Override
  public void listen() throws IOException {
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
            client.close();
            // here i should write an response
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
