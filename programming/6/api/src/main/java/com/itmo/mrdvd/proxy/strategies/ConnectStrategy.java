package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.response.AbstractResponse;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.service.AbstractExecutor;

public class ConnectStrategy implements ProxyStrategy {
  private final AbstractExecutor exec;

  public ConnectStrategy(AbstractExecutor exec) {
    this.exec = exec;
  }

  @Override
  public Response make(Query q) {
    this.exec.processCommand(q.getCmd(), q.getArgs());
    return new AbstractResponse(q.getCmd(), "Подключение установлено.") {};
  }
}
