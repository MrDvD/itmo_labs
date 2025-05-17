package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import java.util.Optional;

public class IgnoreStrategy implements ProxyStrategy {
  @Override
  public Optional<ServiceQuery> make(ServiceQuery q) {
    return Optional.empty();
  }
}
