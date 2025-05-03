package com.itmo.mrdvd.proxy.strategies;

import java.util.List;

import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.proxy.response.AbstractResponse;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.service.executor.AbstractExecutor;

public class InformStrategy implements ProxyStrategy {
  private final AbstractExecutor exec;
  private final String info;

  public InformStrategy(AbstractExecutor exec, String info) {
    this.exec = exec;
    this.info = info;
  }

  @Override
  public Response make(Query q) {
    this.exec.processCommand(q.getCmd(), q.getArgs());
    return new AbstractResponse(q.getCmd(), List.of(this.info)) {};
  }
}
