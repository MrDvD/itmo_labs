package com.itmo.mrdvd.service.shell.queryFillStrategy;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.service.shell.AbstractShell;

public class ConnectQueryStrategy implements QueryFillStrategy {
  private final AbstractShell shell;

  public ConnectQueryStrategy(AbstractShell shell) {
    this.shell = shell;
  }

  @Override
  public ServiceQuery fillArgs(ServiceQuery q) throws IOException {
    Stream<Object> args = q.getArgs().stream();
    Optional<TTY> tty = this.shell.getTty();
    if (tty.isEmpty()) {
      throw new IllegalStateException("Не предоставлен TTY для чтения параметров");
    }
    Optional<String> host = tty.get().getIn().readToken();
    if (host.isPresent()) {
      args = Stream.concat(args, Stream.of(host.get()));
    }
    Optional<Integer> port = tty.get().getIn().readInt();
    if (port.isPresent()) {
      args = Stream.concat(args, Stream.of(port.get()));
    }
    q.setArgs(args.toList());
    return q;
  }
}
