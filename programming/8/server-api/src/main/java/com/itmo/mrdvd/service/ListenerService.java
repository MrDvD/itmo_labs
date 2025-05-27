package com.itmo.mrdvd.service;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.util.function.Function;

/** A service which blindly receives the info and sends the response. */
public interface ListenerService<T> extends Service {
  public void addListener(ServerSocketChannel sock, Function<T, T> callback) throws IOException;
}
