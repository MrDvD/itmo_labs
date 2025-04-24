package com.itmo.mrdvd.proxy;

import java.io.IOException;
import java.net.SocketAddress;
import org.apache.hc.core5.http.ContentType;

public interface ClientProxy extends Proxy {
  /** Connects to the desired server via passed address. */
  public void connect(SocketAddress addr) throws IOException;

  /** Sends a message and returns the response from the server. */
  public String send(String payload, ContentType content) throws RuntimeException;

  /** Sends a message and returns the response from the server. */
  public String send(Object obj) throws RuntimeException;
}
