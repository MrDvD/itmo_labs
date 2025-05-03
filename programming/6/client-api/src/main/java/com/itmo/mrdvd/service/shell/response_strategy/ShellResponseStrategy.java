package com.itmo.mrdvd.service.shell.response_strategy;

import com.itmo.mrdvd.proxy.response.Response;

/** A strategy for Shell to process incoming ResponseDTO. */
public interface ShellResponseStrategy {
  public void make(Response r);
}
