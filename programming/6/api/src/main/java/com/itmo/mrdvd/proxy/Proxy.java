package com.itmo.mrdvd.proxy;

import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.proxy.strategies.ProxyStrategy;

public interface Proxy {
  public Response processQuery(Query r) throws IllegalStateException;

  public void setStrategy(String name, ProxyStrategy s);

  public void setDefaultStrategy(ProxyStrategy s);
}
