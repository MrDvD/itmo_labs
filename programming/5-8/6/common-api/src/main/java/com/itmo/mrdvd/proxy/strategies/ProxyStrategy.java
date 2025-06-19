package com.itmo.mrdvd.proxy.strategies;

import java.util.Optional;

import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;

/** A strategy for Proxy to generate answer-ServiceQuery from request-ServiceQuery. */
public interface ProxyStrategy {
  public Optional<ServiceQuery> make(ServiceQuery q);
}
