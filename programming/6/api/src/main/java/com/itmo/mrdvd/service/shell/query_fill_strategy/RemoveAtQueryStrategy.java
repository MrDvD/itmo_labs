package com.itmo.mrdvd.service.shell.query_fill_strategy;

import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.service.shell.AbstractShell;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class RemoveAtQueryStrategy implements QueryFillStrategy {
  private final AbstractShell shell;

  public RemoveAtQueryStrategy(AbstractShell shell) {
    this.shell = shell;
  }

  @Override
  public Query fillArgs(Query q) throws IOException {
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
