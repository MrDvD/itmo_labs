package com.itmo.mrdvd.service.shell.queryFillStrategy;

import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.service.shell.AbstractShell;
import java.io.IOException;
import java.util.stream.Stream;

public class ShellQueryStrategy implements QueryFillStrategy {
  private final AbstractShell shell;
  private final QueryFillStrategy prev;

  public ShellQueryStrategy(AbstractShell shell) {
    this(shell, null);
  }

  public ShellQueryStrategy(AbstractShell shell, QueryFillStrategy prev) {
    this.shell = shell;
    this.prev = prev;
  }

  @Override
  public ServiceQuery fillArgs(ServiceQuery q) throws IOException {
    if (prev != null) {
      q = prev.fillArgs(q);
    }
    Stream<Object> args = q.getArgs().stream();
    if (this.shell != null) {
      args = Stream.concat(args, Stream.of(this.shell));
    }
    return ServiceQuery.of(q.getName(), args.toList());
  }
}
