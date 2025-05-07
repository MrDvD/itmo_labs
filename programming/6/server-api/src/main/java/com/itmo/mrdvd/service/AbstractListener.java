package com.itmo.mrdvd.service;

import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.response.Response;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.Map;
import java.util.function.Function;

/** A service which blindly receives the info and sends the response. */
public abstract class AbstractListener<T, U, R> implements Service {
  protected final Selector selector;
  protected final Map<SelectionKey, AbstractSelectableChannel> sockets;
  protected final Map<SelectionKey, Function<Query, Response>> callbacks;
  protected final Mapper<? extends T, U> mapper1;
  protected final Mapper<? super R, U> mapper2;

  public AbstractListener(
      Selector selector,
      Mapper<? extends T, U> mapper1,
      Mapper<? super R, U> mapper2,
      Map<SelectionKey, AbstractSelectableChannel> sockets,
      Map<SelectionKey, Function<Query, Response>> callbacks) {
    this.selector = selector;
    this.mapper1 = mapper1;
    this.mapper2 = mapper2;
    this.sockets = sockets;
    this.callbacks = callbacks;
  }

  public void addListener(ServerSocketChannel sock, Function<Query, Response> callback)
      throws IOException {
    sock.configureBlocking(false);
    SelectionKey key = sock.register(selector, SelectionKey.OP_ACCEPT);
    this.sockets.put(key, sock);
    this.callbacks.put(key, callback);
  }
}
