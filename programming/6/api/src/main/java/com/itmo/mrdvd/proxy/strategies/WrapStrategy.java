package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.response.AbstractResponse;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.service.AbstractExecutor;

public class WrapStrategy implements ProxyStrategy {
  private final AbstractExecutor exec;

  public WrapStrategy(AbstractExecutor exec) {
    this.exec = exec;
  }

  @Override
  public Response make(Query q) {
    Object obj = this.exec.processCommand(q.getCmd(), q.getArgs());
    return new AbstractResponse(q.getCmd(), obj) {};
  }
}
