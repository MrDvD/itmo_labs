package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import java.util.List;
import java.util.Optional;

public class WrapStrategy implements ProxyStrategy {
  private final AbstractExecutor exec;

  public WrapStrategy(AbstractExecutor exec) {
    this.exec = exec;
  }

  @Override
  public Optional<ServiceQuery> make(ServiceQuery q) {
    Object obj = this.exec.processCommand(q.getName(), q.getArgs());
    if (obj instanceof List lst) {
      return Optional.of(ServiceQuery.of(q.getName(), lst));
    }
    return Optional.of(ServiceQuery.of(q.getName(), List.of(obj)));
  }
}
