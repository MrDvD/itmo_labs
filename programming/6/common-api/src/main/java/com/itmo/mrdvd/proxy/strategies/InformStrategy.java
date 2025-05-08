package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.service_query.AbstractServiceQuery;
import com.itmo.mrdvd.proxy.service_query.ServiceQuery;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import java.util.List;

public class InformStrategy implements ProxyStrategy {
  private final AbstractExecutor exec;
  private final String info;

  public InformStrategy(AbstractExecutor exec, String info) {
    this.exec = exec;
    this.info = info;
  }

  @Override
  public ServiceQuery make(ServiceQuery q) {
    this.exec.processCommand(q.getName(), q.getArgs());
    return new AbstractServiceQuery(q.getName(), List.of(this.info)) {};
  }
}
