package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.Proxy;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FetchAllStrategy implements ProxyStrategy {
  private final AbstractExecutor exec;
  private final Proxy other;
  private final ProxyStrategy prev;

  public FetchAllStrategy(AbstractExecutor exec, Proxy other) {
    this(exec, other, null);
  }

  public FetchAllStrategy(AbstractExecutor exec, Proxy other, ProxyStrategy prev) {
    this.exec = exec;
    this.other = other;
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
    Optional<ServiceQuery> obj =
        this.other.processQuery(
            ServiceQuery.of(
                q.getName(),
                Stream.concat(
                        Stream.of(q.getArgs().get(q.getArgs().size() - 1)),
                        q.getArgs().stream().limit(q.getArgs().size() - 1))
                    .toList()));
    if (obj.isEmpty()) {
      return Optional.empty();
    }
    List<?> left = obj.get().getArgs();
    List<?> right = (List) this.exec.processCommand(q.getName(), q.getArgs());
    return Optional.of(
        ServiceQuery.of(q.getName(), Stream.concat(left.stream(), right.stream()).toList()));
  }
}
