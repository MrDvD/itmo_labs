package com.itmo.mrdvd.proxy;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

public interface ServerProxy extends Proxy {
  public void addListener(ServerSocketChannel sock) throws IOException;

  public void listen() throws IOException;

  public void close();
}
