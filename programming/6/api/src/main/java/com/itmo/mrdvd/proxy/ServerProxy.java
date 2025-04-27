package com.itmo.mrdvd.proxy;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.util.Map;

import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.executor.commands.response.Response;
import com.itmo.mrdvd.executor.queries.Query;

public interface ServerProxy extends Proxy {
  public void addListener(ServerSocketChannel sock) throws IOException;

  public Response processQuery(Query q, Map<String, Command<?>> commands);

  public void listen() throws IOException;

  public void close();
}
