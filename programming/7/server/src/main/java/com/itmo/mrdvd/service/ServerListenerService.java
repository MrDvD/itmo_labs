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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Function;

public class ServerListenerService<T> implements ListenerService<T> {
  protected final Selector selector;
  protected final ConnectionAcceptor acceptor;
  protected final ClientHandler handler;
  protected final Map<SelectionKey, AbstractSelectableChannel> sockets;
  protected final int bufferSize;
  protected final ReadWriteLock selectorLock;
  protected final ReadWriteLock socketsLock;
  protected final ExecutorService forkJoinPool;
  protected final ExecutorService cachedThreadPool;
  protected volatile boolean isOpen;

  public ServerListenerService(
      Selector selector,
      ConnectionAcceptor acceptor,
      ClientHandler handler,
      int bufferSize,
      ReadWriteLock selectorLock,
      ReadWriteLock socketsLock,
      ExecutorService forkJoinPool,
      ExecutorService cachedThreadPool) {
    this(
        selector,
        acceptor,
        handler,
        bufferSize,
        selectorLock,
        socketsLock,
        forkJoinPool,
        cachedThreadPool,
        new HashMap<>());
  }

  public ServerListenerService(
      Selector selector,
      ConnectionAcceptor acceptor,
      ClientHandler handler,
      int bufferSize,
      ReadWriteLock selectorLock,
      ReadWriteLock socketsLock,
      ExecutorService forkJoinPool,
      ExecutorService cachedThreadPool,
      Map<SelectionKey, AbstractSelectableChannel> sockets) {
    this.selector = selector;
    this.sockets = sockets;
    this.acceptor = acceptor.setSockets(this.sockets);
    this.acceptor.setExecutorService(cachedThreadPool);
    this.handler = handler;
    this.handler.setExecutorService(forkJoinPool);
    this.selectorLock = selectorLock;
    this.socketsLock = socketsLock;
    this.forkJoinPool = forkJoinPool;
    this.cachedThreadPool = cachedThreadPool;
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
  public void start() {
    this.isOpen = true;
    try {
      while (this.isOpen) {
        this.selector.select();
        if (!this.isOpen) {
          break;
        }
        Set<SelectionKey> keys = selector.selectedKeys();
        for (SelectionKey key : keys) {
          if (key.isAcceptable()) {
            this.acceptor.acceptConnection(key);
          }
          if (key.isReadable()) {
            this.handler.handleClient(key, ByteBuffer.allocate(this.bufferSize));
          }
          keys.remove(key);
        }
      }
    } catch (IOException | RuntimeException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        this.cachedThreadPool.shutdown();
        this.forkJoinPool.shutdown();
        for (AbstractSelectableChannel ch : this.sockets.values()) {
          ch.close();
        }
        this.selector.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void stop() {
    this.isOpen = false;
    selector.wakeup();
  }
}
