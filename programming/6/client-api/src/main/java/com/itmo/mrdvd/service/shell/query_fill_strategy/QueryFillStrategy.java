package com.itmo.mrdvd.service.shell.query_fill_strategy;

import com.itmo.mrdvd.proxy.service_query.ServiceQuery;
import java.io.IOException;

/** A strategy for Shell to fill QueryDTO with args. */
public interface QueryFillStrategy {
  public ServiceQuery fillArgs(ServiceQuery q) throws IOException;
}
