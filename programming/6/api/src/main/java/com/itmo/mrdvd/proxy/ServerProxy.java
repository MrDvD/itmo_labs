package com.itmo.mrdvd.proxy;

import com.itmo.mrdvd.executor.queries.Query;
import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.util.function.Function;

public interface ServerProxy extends Proxy {
  public void addListener(ServerSocketChannel sock) throws IOException;

  public void setCallback(Function<Query, Query> callback);

  public void listen() throws IOException;

  public void close();
}
