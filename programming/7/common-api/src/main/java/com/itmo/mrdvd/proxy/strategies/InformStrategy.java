package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import java.util.List;
import java.util.Optional;

public class InformStrategy implements ProxyStrategy {
  private final AbstractExecutor exec;
  private final String info;

  public InformStrategy(AbstractExecutor exec, String info) {
    this.exec = exec;
    this.info = info;
  }

  @Override
  public Optional<ServiceQuery> make(ServiceQuery q) {
    this.exec.processCommand(q.getName(), q.getArgs());
    return Optional.of(ServiceQuery.of(q.getName(), List.of(this.info)));
  }
}
