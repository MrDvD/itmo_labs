package com.itmo.mrdvd;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.Map;
import java.util.function.Function;

import com.itmo.mrdvd.proxy.Mapper;
import com.itmo.mrdvd.proxy.Service;

/**
 * A service which blindly receives the info and sends the response.
 */
public abstract class AbstractListener<T, U, R> implements Service {
  protected final Selector selector;
  protected final Map<SelectionKey, AbstractSelectableChannel> sockets;
  protected final Mapper<T, U> mapper1;
  protected final Mapper<R, U> mapper2;
  protected final Function<T, R> callback;

  public AbstractListener(Selector selector, Function<T, R> callback, Mapper<T, U> mapper1, Mapper<R, U> mapper2, Map<SelectionKey, AbstractSelectableChannel> sockets) {
    this.selector = selector;
    this.mapper1 = mapper1;
    this.mapper2 = mapper2;
    this.sockets = sockets;
    this.callback = callback;
  }

  public void addListener(ServerSocketChannel sock) throws IOException {
    sock.configureBlocking(false);
    this.sockets.put(sock.register(selector, SelectionKey.OP_ACCEPT), sock);
  }
}
