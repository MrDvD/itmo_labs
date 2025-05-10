package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.Proxy;
import com.itmo.mrdvd.proxy.service_query.AbstractServiceQuery;
import com.itmo.mrdvd.proxy.service_query.ServiceQuery;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FetchAllStrategy implements ProxyStrategy {
  private final AbstractExecutor exec;
  private final Proxy other;

  public FetchAllStrategy(AbstractExecutor exec, Proxy other) {
    this.exec = exec;
    this.other = other;
  }

  @Override
  public Optional<ServiceQuery> make(ServiceQuery q) {
    Optional<ServiceQuery> obj = this.other.processQuery(q);
    if (obj.isEmpty()) {
      return Optional.empty();
    }
    List<?> left = obj.get().getArgs();
    List<?> right = (List) this.exec.processCommand(q.getName(), q.getArgs());
    return Optional.of(
        new AbstractServiceQuery(
            q.getName(), Stream.concat(left.stream(), right.stream()).toList()) {});
  }
}
