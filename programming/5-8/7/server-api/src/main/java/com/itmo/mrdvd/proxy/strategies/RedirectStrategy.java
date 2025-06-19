package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.Proxy;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import java.util.Optional;

public class RedirectStrategy implements ProxyStrategy {
  private final Proxy other;

  public RedirectStrategy(Proxy other) {
    this.other = other;
  }

  @Override
  public Optional<ServiceQuery> make(ServiceQuery q) {
    return this.other.processQuery(q);
  }
}
