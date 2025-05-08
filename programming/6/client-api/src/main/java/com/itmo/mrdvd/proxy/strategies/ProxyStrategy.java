package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.service_query.ServiceQuery;

/** A strategy for Proxy to generate ResponseDTO for QueryDTO. */
public interface ProxyStrategy {
  public ServiceQuery make(ServiceQuery q);
}
