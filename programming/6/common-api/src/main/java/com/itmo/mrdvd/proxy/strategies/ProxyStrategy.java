package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.service_query.ServiceQuery;

/** A strategy for Proxy to generate answer-ServiceQuery from request-ServiceQuery. */
public interface ProxyStrategy {
  public ServiceQuery make(ServiceQuery q);
}
