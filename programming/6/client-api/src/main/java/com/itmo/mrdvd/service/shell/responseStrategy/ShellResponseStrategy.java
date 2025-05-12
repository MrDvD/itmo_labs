package com.itmo.mrdvd.service.shell.responseStrategy;

import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;

/** A strategy for Shell to process incoming ResponseDTO. */
public interface ShellResponseStrategy {
  public void make(ServiceQuery r);
}
