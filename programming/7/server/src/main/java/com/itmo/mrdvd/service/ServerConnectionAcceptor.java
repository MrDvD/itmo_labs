package com.itmo.mrdvd.service;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReadWriteLock;

public class ServerConnectionAcceptor implements ConnectionAcceptor {
  private final Map<SelectionKey, AbstractSelectableChannel> sockets;
  private final Selector selector;
  private final ExecutorService connectionPool;
  private final ReadWriteLock socketsLock;
  private final ReadWriteLock selectorLock;

  public ServerConnectionAcceptor(
      Selector selector,
      ExecutorService connectionPool,
      ReadWriteLock selectorLock,
      ReadWriteLock socketsLock) {
    this(selector, connectionPool, selectorLock, socketsLock, new HashMap<>());
  }

  public ServerConnectionAcceptor(
      Selector selector,
      ExecutorService connectionPool,
      ReadWriteLock selectorLock,
      ReadWriteLock socketsLock,
      Map<SelectionKey, AbstractSelectableChannel> sockets) {
    this.selector = selector;
    this.connectionPool = connectionPool;
    this.socketsLock = socketsLock;
    this.selectorLock = selectorLock;
    this.sockets = sockets;
  }

  @Override
  public void acceptConnection(SelectionKey key) {
    // System.out.println("Getting selectorLock in accept");
    this.selectorLock.writeLock().lock();
    key.interestOps(key.interestOps() & ~SelectionKey.OP_ACCEPT);
    key.selector().wakeup();
    this.selectorLock.writeLock().unlock();
    // System.out.println("Released selectorLock in accept");
    this.connectionPool.submit(
        () -> {
          try {
            // System.out.println("Trying to accept connection.");
            this.socketsLock.readLock().lock();
            SocketChannel client = ((ServerSocketChannel) this.sockets.get(key)).accept();
            this.socketsLock.readLock().unlock();
            if (client != null) {
              client.configureBlocking(false);
              // System.out.println("Trying to get the SelectorLock");
              this.selectorLock.writeLock().lock();
              SelectionKey clientKey = client.register(this.selector, SelectionKey.OP_READ);
              clientKey.attach(key.attachment());
              this.selector.wakeup();
              this.selectorLock.writeLock().unlock();
              // System.out.println("Released the SelectorLock");
              System.out.println("Accepted connection from " + client.getRemoteAddress());
            }
          } catch (IOException e) {
            throw new RuntimeException(e);
          } finally {
            // System.out.println("finally Getting selectorLock in accept");
            this.selectorLock.writeLock().lock();
            if (key.isValid()) {
              key.interestOps(key.interestOps() | SelectionKey.OP_ACCEPT);
              key.selector().wakeup();
            }
            this.selectorLock.writeLock().unlock();
            // System.out.println("finally releasing selectorLock in accept");
          }
        });
  }

  @Override
  public ServerConnectionAcceptor setSockets(Map<SelectionKey, AbstractSelectableChannel> sockets) {
    return new ServerConnectionAcceptor(
        this.selector, this.connectionPool, this.selectorLock, this.socketsLock, sockets);
  }
}
