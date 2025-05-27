package com.itmo.mrdvd.service;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.Map;

public interface ConnectionAcceptor extends MultithreadModule {
  public void acceptConnection(SelectionKey key) throws IOException;

  public ConnectionAcceptor setSockets(Map<SelectionKey, AbstractSelectableChannel> sockets);
}
