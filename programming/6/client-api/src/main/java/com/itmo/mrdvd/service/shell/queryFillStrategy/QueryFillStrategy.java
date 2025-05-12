package com.itmo.mrdvd.service.shell.queryFillStrategy;

import java.io.IOException;

import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;

/** A strategy for Shell to fill QueryDTO with args. */
public interface QueryFillStrategy {
  public ServiceQuery fillArgs(ServiceQuery q) throws IOException;
}
