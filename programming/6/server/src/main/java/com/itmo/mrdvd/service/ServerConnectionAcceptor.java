package com.itmo.mrdvd.service;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ServerConnectionAcceptor implements ConnectionAcceptor {
  private final Map<SelectionKey, AbstractSelectableChannel> sockets;
  private final Selector selector;

  public ServerConnectionAcceptor(Selector selector) {
    this(selector, new HashMap<>());
  }

  public ServerConnectionAcceptor(Selector selector, Map<SelectionKey, AbstractSelectableChannel> sockets) {
    this.selector = selector;
    this.sockets = sockets;
  }

  @Override
  public void acceptConnection(SelectionKey key) throws IOException {
    SocketChannel client = ((ServerSocketChannel) this.sockets.get(key)).accept();
    if (client != null) {
      client.configureBlocking(false);
      SelectionKey clientKey = client.register(this.selector, SelectionKey.OP_READ);
      clientKey.attach(key.attachment());
      System.out.println("Accepted connection from " + client.getRemoteAddress());
    }
  }

  @Override
  public ServerConnectionAcceptor setSockets(Map<SelectionKey, AbstractSelectableChannel> sockets) {
    return new ServerConnectionAcceptor(this.selector, sockets);
  }
}
