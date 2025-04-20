package com.itmo.mrdvd.proxy;

import org.apache.hc.core5.http.ContentType;

public interface ClientProxy extends Proxy {
  /** Sends a message and returns the response from the server. */
  public String send(String payload, ContentType content) throws RuntimeException;

  /** Sends a message and returns the response from the server. */
  public String send(Object obj) throws RuntimeException;
}
