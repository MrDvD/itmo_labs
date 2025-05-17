package com.itmo.mrdvd.service.shell.queryFillStrategy;

import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.service.shell.AbstractShell;
import java.io.IOException;
import java.util.Optional;

public class SkipLineStrategy implements QueryFillStrategy {
  private final AbstractShell shell;
  private final QueryFillStrategy prev;

  public SkipLineStrategy(AbstractShell shell) {
    this(shell, null);
  }

  public SkipLineStrategy(AbstractShell shell, QueryFillStrategy prev) {
    this.shell = shell;
    this.prev = prev;
  }

  @Override
  public ServiceQuery fillArgs(ServiceQuery q) throws IOException {
    if (prev != null) {
      q = prev.fillArgs(q);
    }
    Optional<TTY> tty = this.shell.getTty();
    if (tty.isEmpty()) {
      throw new IllegalStateException("Не предоставлен TTY для чтения параметров.");
    }
    tty.get().getIn().skipLine();
    return q;
  }
}
