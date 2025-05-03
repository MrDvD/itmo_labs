package com.itmo.mrdvd.service.shell.query_fill_strategy;

import com.itmo.mrdvd.proxy.Query;
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
  public Query fillArgs(Query q) throws IOException {
    if (prev != null) {
      q = prev.fillArgs(q);
    }
    Stream<Object> args = q.getArgs().stream();
    if (this.shell != null) {
      args = Stream.concat(args, Stream.of(this.shell));
    }
    q.setArgs(args.toList());
    return q;
  }
}
