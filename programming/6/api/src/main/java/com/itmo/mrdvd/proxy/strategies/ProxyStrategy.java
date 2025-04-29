package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.response.Response;

/**
 * A strategy for Proxy to generate ResponseDTO for QueryDTO.
 */
public interface ProxyStrategy {
  public Response make(Query q);
}
