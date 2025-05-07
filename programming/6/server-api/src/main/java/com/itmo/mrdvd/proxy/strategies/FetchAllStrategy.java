package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.Proxy;
import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.proxy.response.AbstractResponse;
import com.itmo.mrdvd.proxy.response.Response;
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
  public Response make(Query q) {
    Response obj = this.other.processQuery(q);
    List<Query> left = (List) obj.getBody();
    List<Query> right = (List) this.exec.processCommand(q.getCmd(), q.getArgs());
    return new AbstractResponse(
        q.getCmd(), (List) Stream.concat(left.stream(), right.stream()).toList()) {};
  }
}
