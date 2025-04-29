package com.itmo.mrdvd.service.shell.query_fill_strategy;

import java.io.IOException;
import java.util.stream.Stream;

import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.service.Service;

public class ExitQueryStrategy implements QueryFillStrategy {
  private final Service service;

  public ExitQueryStrategy(Service service) {
    this.service = service;
  }

  @Override
  public Query fillArgs(Query q) throws IOException {
    Stream<Object> args = q.getArgs().stream();
    if (service != null) {
      args = Stream.concat(args, Stream.of(this.service));
    }
    q.setArgs(args.toList());
    return q;
  }
}
