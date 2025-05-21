package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import java.util.List;
import java.util.Optional;

public class WrapStrategy implements ProxyStrategy {
  private final AbstractExecutor exec;
  private final ProxyStrategy prev;

  public WrapStrategy(AbstractExecutor exec) {
    this(exec, null);
  }

  public WrapStrategy(AbstractExecutor exec, ProxyStrategy prev) {
    this.exec = exec;
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
    Object obj = this.exec.processCommand(q.getName(), q.getArgs());
    if (obj instanceof List lst) {
      return Optional.of(ServiceQuery.of(q.getName(), lst));
    }
    return Optional.of(ServiceQuery.of(q.getName(), List.of(obj)));
  }
}
