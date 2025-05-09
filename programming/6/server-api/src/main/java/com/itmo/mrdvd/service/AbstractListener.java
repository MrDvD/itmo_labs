package com.itmo.mrdvd.service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.itmo.mrdvd.proxy.mappers.Mapper;

/** A service which blindly receives the info and sends the response. */
public abstract class AbstractListener<T> implements Service {
  protected final Selector selector;
  protected final Map<SelectionKey, AbstractSelectableChannel> sockets;
  protected final Map<SelectionKey, ByteBuffer> buffers;
  protected final Map<SelectionKey, Function<T, T>> callbacks;
  protected final Mapper<? super T, String> serialPacket;
  protected final Mapper<String, ? extends T> deserialPacket;
  protected boolean isOpen;

  public AbstractListener(
      Selector selector,
      Mapper<String, ? extends T> deserialPacket,
      Mapper<? super T, String> serialPacket,
      Map<SelectionKey, AbstractSelectableChannel> sockets,
      Map<SelectionKey, ByteBuffer> buffers,
      Map<SelectionKey, Function<T, T>> callbacks) {
    this.selector = selector;
    this.deserialPacket = deserialPacket;
    this.serialPacket = serialPacket;
    this.sockets = sockets;
    this.buffers = buffers;
    this.callbacks = callbacks;
  }

  public void addListener(ServerSocketChannel sock, Function<T, T> callback) throws IOException {
    sock.configureBlocking(false);
    SelectionKey key = sock.register(selector, SelectionKey.OP_ACCEPT);
    this.sockets.put(key, sock);
    this.callbacks.put(key, callback);
  }

  /** Waits for incoming connections in a non-blocking way. */
  @Override
  public void start() throws IllegalStateException, RuntimeException {
    if (this.deserialPacket == null) {
      throw new IllegalStateException("Не предоставлен маппер входящих пакетов.");
    }
    if (this.serialPacket == null) {
      throw new IllegalStateException("Не предоставлен маппер исходящих пакетов.");
    }
    this.isOpen = true;
    try {
      while (this.isOpen) {
        this.selector.select();
        Set<SelectionKey> keys = selector.selectedKeys();
        for (SelectionKey key : keys) {
          if (key.isAcceptable()) {
            acceptConnection(key);
          }
          if (key.isReadable()) {
            handleConnection(key, this.buffers.get(key));
            this.sockets.remove(key);
            this.buffers.remove(key);
          }
          keys.remove(key);
        }
      }
    } catch (IOException e) {}
  }

  public abstract void acceptConnection(SelectionKey key) throws IOException;

  public abstract void handleConnection(SelectionKey key, ByteBuffer buffer) throws IOException;

  public abstract void sendResponse(SocketChannel client, String response) throws IOException;

  @Override
  public void stop() {
    this.isOpen = false;
  }
}
