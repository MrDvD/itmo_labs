package com.itmo.mrdvd.proxy;

import java.util.Optional;

import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.proxy.strategies.ProxyStrategy;

public interface Proxy {
  public Optional<ServiceQuery> processQuery(ServiceQuery r) throws IllegalStateException;

  public void setStrategy(String name, ProxyStrategy s);

  public void setDefaultStrategy(ProxyStrategy s);
}
