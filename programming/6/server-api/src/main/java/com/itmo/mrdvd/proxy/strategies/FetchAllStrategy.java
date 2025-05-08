package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.Proxy;
import com.itmo.mrdvd.proxy.service_query.AbstractServiceQuery;
import com.itmo.mrdvd.proxy.service_query.ServiceQuery;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import java.util.List;
import java.util.stream.Stream;

public class FetchAllStrategy implements ProxyStrategy {
  private final AbstractExecutor exec;
  private final Proxy other;

  public FetchAllStrategy(AbstractExecutor exec, Proxy other) {
    this.exec = exec;
    this.other = other;
  }

  @Override
  public ServiceQuery make(ServiceQuery q) {
    ServiceQuery obj = this.other.processQuery(q);
    List<?> left = obj.getArgs();
    List<?> right = (List) this.exec.processCommand(q.getName(), q.getArgs());
    return new AbstractServiceQuery(
        q.getName(), Stream.concat(left.stream(), right.stream()).toList()) {};
  }
}
