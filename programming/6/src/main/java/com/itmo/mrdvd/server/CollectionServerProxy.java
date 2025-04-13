package com.itmo.mrdvd.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.ServerSocketChannel;
import java.util.List;
import java.util.Optional;

import com.itmo.mrdvd.proxy.ServerProxy;
import com.itmo.mrdvd.proxy.TransportProtocol;

public class CollectionServerProxy implements ServerProxy {
  protected Optional<ServerSocketChannel> socket;
  protected TransportProtocol protocol;
  protected Optional<InputStreamReader> in;
  protected Optional<OutputStreamWriter> out;

  public CollectionServerProxy(ServerSocketChannel sock, TransportProtocol proto) {
    this.protocol = proto;
    try {
      sock.configureBlocking(false);
      this.socket = Optional.of(sock);
    } catch (IOException e) {
      this.socket = Optional.empty();
    }
  }

  @Override
  public void setProtocol(TransportProtocol proto) {
    if (proto != null) {
      this.protocol = proto;
    }
  }

  @Override
  public addListener(ServerSocketChannel sock) {
    // ...
  }

  /**
   * Waits for incoming connections in a non-blocking way.
   */
  @Override
  public void listen() {
  }
}
