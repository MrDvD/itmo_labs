package com.itmo.mrdvd.proxy;

import com.itmo.mrdvd.executor.commands.response.Response;

public interface ProxyStrategy {
  public Response make();
}
