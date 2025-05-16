package com.itmo.mrdvd.service.shell.queryFillStrategy;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.service.shell.AbstractShell;

public class ReadLongQueryStrategy implements QueryFillStrategy {
  private final AbstractShell shell;
  private final QueryFillStrategy prev;

  public ReadLongQueryStrategy(AbstractShell shell) {
    this(shell, null);
  }

  public ReadLongQueryStrategy(AbstractShell shell, QueryFillStrategy prev) {
    this.shell = shell;
    this.prev = prev;
  }

  @Override
  public ServiceQuery fillArgs(ServiceQuery q) throws IOException {
    if (prev != null) {
      q = prev.fillArgs(q);
    }
    Stream<Object> args = q.getArgs().stream();
    Optional<TTY> tty = this.shell.getTty();
    if (tty.isEmpty()) {
      throw new IllegalStateException("Не предоставлен TTY для чтения параметров.");
    }
    Optional<Long> idx = tty.get().getIn().readLong();
    if (idx.isPresent()) {
      args = Stream.concat(args, Stream.of(idx.get()));
    }
    q.setArgs(args.toList());
    return q;
  }
}
