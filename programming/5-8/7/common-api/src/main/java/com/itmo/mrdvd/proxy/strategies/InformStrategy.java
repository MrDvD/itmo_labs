package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import java.util.List;
import java.util.Optional;

public class InformStrategy implements ProxyStrategy {
  private final AbstractExecutor exec;
  private final String info;
  private final ProxyStrategy prev;

  public InformStrategy(AbstractExecutor exec, String info) {
    this(exec, info, null);
  }

  public InformStrategy(AbstractExecutor exec, String info, ProxyStrategy prev) {
    this.exec = exec;
    this.info = info;
    this.prev = prev;
  }

  @Override
  public Optional<ServiceQuery> make(ServiceQuery q) {
    if (this.prev != null) {
      Optional<ServiceQuery> newQ = this.prev.make(q);
      if (newQ.isPresent() && newQ.get().getName().equals(q.getName())) {
        q = newQ.get();
      } else {
        return newQ;
      }
    }
    this.exec.processCommand(q.getName(), q.getArgs());
    return Optional.of(ServiceQuery.of(q.getName(), List.of(this.info)));
  }
}
