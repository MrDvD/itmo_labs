package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.proxy.response.AbstractResponse;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import java.util.List;

public class WrapStrategy implements ProxyStrategy {
  private final AbstractExecutor exec;

  public WrapStrategy(AbstractExecutor exec) {
    this.exec = exec;
  }

  @Override
  public Response make(Query q) {
    Object obj = this.exec.processCommand(q.getCmd(), q.getArgs());
    if (obj instanceof List lst) {
      return new AbstractResponse(q.getCmd(), lst) {};
    }
    return new AbstractResponse(q.getCmd(), List.of(obj)) {};
  }
}
