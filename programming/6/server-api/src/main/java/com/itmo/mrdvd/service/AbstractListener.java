package com.itmo.mrdvd.service;

import com.itmo.mrdvd.proxy.mappers.Mapper;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.Map;
import java.util.function.Function;

/** A service which blindly receives the info and sends the response. */
public abstract class AbstractListener<T> implements Service {
  protected final Selector selector;
  protected final Map<SelectionKey, AbstractSelectableChannel> sockets;
  protected final Map<SelectionKey, Function<T, T>> callbacks;
  protected final Mapper<? super T, String> serialPacket;
  protected final Mapper<String, ? extends T> deserialPacket;

  public AbstractListener(
      Selector selector,
      Mapper<String, ? extends T> deserialPacket,
      Mapper<? super T, String> serialPacket,
      Map<SelectionKey, AbstractSelectableChannel> sockets,
      Map<SelectionKey, Function<T, T>> callbacks) {
    this.selector = selector;
    this.deserialPacket = deserialPacket;
    this.serialPacket = serialPacket;
    this.sockets = sockets;
    this.callbacks = callbacks;
  }

  public void addListener(ServerSocketChannel sock, Function<T, T> callback) throws IOException {
    sock.configureBlocking(false);
    SelectionKey key = sock.register(selector, SelectionKey.OP_ACCEPT);
    this.sockets.put(key, sock);
    this.callbacks.put(key, callback);
  }
}
