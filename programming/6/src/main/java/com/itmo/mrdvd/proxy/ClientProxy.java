package com.itmo.mrdvd.proxy;

import java.net.Socket;

public interface ClientProxy extends Proxy {
  public void addSender(Socket sock);

  public Socket getSender();
}
