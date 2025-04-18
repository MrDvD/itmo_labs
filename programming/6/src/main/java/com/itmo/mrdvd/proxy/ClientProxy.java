package com.itmo.mrdvd.proxy;

import org.apache.hc.core5.http.ContentType;

public interface ClientProxy extends Proxy {

  public void send(String payload, ContentType content) throws RuntimeException;

  public void send(Object obj) throws RuntimeException;
}
