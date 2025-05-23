package com.itmo.mrdvd.service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class ServerListenerService<T> implements ListenerService<T> {
  protected final Selector selector;
  protected final ConnectionAcceptor acceptor;
  protected final ClientHandler handler;
  protected final Map<SelectionKey, AbstractSelectableChannel> sockets;
  protected final int bufferSize;
  protected boolean isOpen;

  public ServerListenerService(
      Selector selector, ConnectionAcceptor acceptor, ClientHandler handler, int bufferSize) {
    this(selector, acceptor, handler, bufferSize, new HashMap<>());
  }

  public ServerListenerService(
      Selector selector,
      ConnectionAcceptor acceptor,
      ClientHandler handler,
      int bufferSize,
      Map<SelectionKey, AbstractSelectableChannel> sockets) {
    this.selector = selector;
    this.sockets = sockets;
    this.acceptor = acceptor.setSockets(this.sockets);
    this.handler = handler;
    this.bufferSize = bufferSize;
  }

  @Override
  public void addListener(ServerSocketChannel sock, Function<T, T> callback) throws IOException {
    sock.configureBlocking(false);
    SelectionKey key = sock.register(selector, SelectionKey.OP_ACCEPT);
    key.attach(callback);
    this.sockets.put(key, sock);
  }

  /** Waits for incoming connections in a non-blocking way. */
  @Override
  public void start() throws IllegalStateException, RuntimeException {
    this.isOpen = true;
    try {
      while (this.isOpen) {
        this.selector.select();
        Set<SelectionKey> keys = selector.selectedKeys();
        for (SelectionKey key : keys) {
          if (key.isAcceptable()) {
            this.acceptor.acceptConnection(key);
          }
          if (key.isReadable()) {
            this.handler.handleClient(key, ByteBuffer.allocate(this.bufferSize));
            this.sockets.remove(key);
          }
          keys.remove(key);
        }
      }
    } catch (IOException e) {
    }
  }

  @Override
  public void stop() {
    this.isOpen = false;
  }
}
