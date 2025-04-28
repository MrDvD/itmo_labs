package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.response.AbstractResponse;
import com.itmo.mrdvd.proxy.response.EmptyResponse;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.service.AbstractExecutor;

public class LocalStrategy implements ProxyStrategy {
  private final AbstractExecutor exec;

  public LocalStrategy(AbstractExecutor exec) {
    this.exec = exec;
  }

  @Override
  public Response make(Query q) {
    Object result = this.exec.processCommand(q.getCmd(), q.getArgs());
    if (result != null) {
      return new AbstractResponse("ok", result) {};
    }
    return new EmptyResponse();
  }
}
