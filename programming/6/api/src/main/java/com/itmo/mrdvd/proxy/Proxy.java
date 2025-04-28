package com.itmo.mrdvd.proxy;

import com.itmo.mrdvd.executor.commands.response.Response;
import com.itmo.mrdvd.executor.queries.Query;

public interface Proxy {
  public Response processQuery(Query r);

  public void setStrategy(String name, ProxyStrategy stat);
}
