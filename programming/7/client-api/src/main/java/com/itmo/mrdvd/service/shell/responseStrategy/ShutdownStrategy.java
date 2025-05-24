package com.itmo.mrdvd.service.shell.responseStrategy;

import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.service.shell.AbstractShell;

public class ShutdownStrategy implements ShellResponseStrategy {
  private final AbstractShell shell;
  private final ShellResponseStrategy prev;

  public ShutdownStrategy(AbstractShell shell) {
    this(shell, null);
  }

  public ShutdownStrategy(AbstractShell shell, ShellResponseStrategy prev) {
    this.shell = shell;
    this.prev = prev;
  }

  @Override
  public void make(ServiceQuery r) throws IllegalStateException {
    if (this.prev != null) {
      this.prev.make(r);
    }
    this.shell.stop();
  }
}
