package com.itmo.mrdvd.service.shell.query_fill_strategy;

import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.service.shell.AbstractShell;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class ReadIntQueryStrategy implements QueryFillStrategy {
  private final AbstractShell shell;
  private final QueryFillStrategy prev;

  public ReadIntQueryStrategy(AbstractShell shell) {
    this(shell, null);
  }

  public ReadIntQueryStrategy(AbstractShell shell, QueryFillStrategy prev) {
    this.shell = shell;
    this.prev = prev;
  }

  @Override
  public Query fillArgs(Query q) throws IOException {
    if (prev != null) {
      q = prev.fillArgs(q);
    }
    Stream<Object> args = q.getArgs().stream();
    Optional<TTY> tty = this.shell.getTty();
    if (tty.isEmpty()) {
      throw new IllegalStateException("Не предоставлен TTY для чтения параметров");
    }
    Optional<Integer> idx = tty.get().getIn().readInt();
    if (idx.isPresent()) {
      args = Stream.concat(args, Stream.of(idx.get()));
    }
    q.setArgs(args.toList());
    return q;
  }
}
