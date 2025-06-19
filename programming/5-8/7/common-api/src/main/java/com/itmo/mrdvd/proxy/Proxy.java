package com.itmo.mrdvd.proxy;

import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.proxy.strategies.ProxyStrategy;
import java.util.Optional;

public interface Proxy {
  public Optional<ServiceQuery> processQuery(ServiceQuery r) throws IllegalStateException;

  public void setStrategy(String name, ProxyStrategy s);

  public void setDefaultStrategy(ProxyStrategy s);
}
