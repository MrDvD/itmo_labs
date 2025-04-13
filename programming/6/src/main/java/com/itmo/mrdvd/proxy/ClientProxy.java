package com.itmo.mrdvd.proxy;

public interface ClientProxy extends Proxy {
  public void send(String payload) throws RuntimeException;
}
