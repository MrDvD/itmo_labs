package com.itmo.mrdvd.proxy.strategies;

import java.util.Optional;

import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;

public class IgnoreStrategy implements ProxyStrategy {
  @Override
  public Optional<ServiceQuery> make(ServiceQuery q) {
    return Optional.empty();
  }
}
