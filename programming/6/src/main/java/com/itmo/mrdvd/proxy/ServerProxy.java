package com.itmo.mrdvd.proxy;

import java.net.ServerSocket;

public interface ServerProxy extends Proxy {
  public void addListener(ServerSocket sock);

  public void listen();
}
