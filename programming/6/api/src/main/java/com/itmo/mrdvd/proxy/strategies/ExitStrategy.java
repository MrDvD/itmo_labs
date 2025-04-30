package com.itmo.mrdvd.proxy.strategies;

import java.util.List;

import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.proxy.response.AbstractResponse;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.service.executor.AbstractExecutor;

public class ExitStrategy implements ProxyStrategy {
  private final AbstractExecutor exec;

  public ExitStrategy(AbstractExecutor exec) {
    this.exec = exec;
  }

  @Override
  public Response make(Query q) {
    this.exec.processCommand(q.getCmd(), q.getArgs());
    return new AbstractResponse(q.getCmd(), List.of("Производится выход...")) {};
  }
}
