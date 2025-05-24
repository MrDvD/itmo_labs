package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import java.util.Optional;

/** A strategy for Proxy to generate answer-ServiceQuery from request-ServiceQuery. */
public interface ProxyStrategy {
  public Optional<ServiceQuery> make(ServiceQuery q);
}
