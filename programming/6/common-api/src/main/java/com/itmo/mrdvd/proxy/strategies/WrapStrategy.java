package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.service_query.AbstractServiceQuery;
import com.itmo.mrdvd.proxy.service_query.ServiceQuery;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import java.util.List;

public class WrapStrategy implements ProxyStrategy {
  private final AbstractExecutor exec;

  public WrapStrategy(AbstractExecutor exec) {
    this.exec = exec;
  }

  @Override
  public ServiceQuery make(ServiceQuery q) {
    Object obj = this.exec.processCommand(q.getName(), q.getArgs());
    if (obj instanceof List lst) {
      return new AbstractServiceQuery(q.getName(), lst) {};
    }
    return new AbstractServiceQuery(q.getName(), List.of(obj)) {};
  }
}
