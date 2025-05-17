package com.itmo.mrdvd.service.shell.queryFillStrategy;

import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import java.io.IOException;

/** A strategy for Shell to fill QueryDTO with args. */
public interface QueryFillStrategy {
  public ServiceQuery fillArgs(ServiceQuery q) throws IOException;
}
