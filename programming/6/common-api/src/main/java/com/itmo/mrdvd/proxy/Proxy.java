package com.itmo.mrdvd.proxy;

import com.itmo.mrdvd.proxy.service_query.ServiceQuery;
import com.itmo.mrdvd.proxy.strategies.ProxyStrategy;

public interface Proxy {
  public ServiceQuery processQuery(ServiceQuery r) throws IllegalStateException;

  public void setStrategy(String name, ProxyStrategy s);

  public void setDefaultStrategy(ProxyStrategy s);
}
