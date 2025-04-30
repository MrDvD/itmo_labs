package com.itmo.mrdvd.service.shell;

import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.service.Service;
import com.itmo.mrdvd.service.shell.query_fill_strategy.QueryFillStrategy;
import com.itmo.mrdvd.service.shell.response_strategy.ShellResponseStrategy;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractShell implements Service {
  private final Map<String, QueryFillStrategy> requestArgs;
  protected final Map<String, ShellResponseStrategy> strats;
  protected ShellResponseStrategy defaultStrat;
  private final List<TTY> tty;

  public AbstractShell(
      List<TTY> tty,
      Map<String, QueryFillStrategy> args,
      Map<String, ShellResponseStrategy> strats) {
    this.tty = tty;
    this.requestArgs = args;
    this.strats = strats;
  }

  /**
   * Automatically sets the args into the Query object with the given name.
   *
   * <p>The relevance of args is up to the implementation of the class.
   */
  public void setQueryStrategy(String name, QueryFillStrategy q) {
    this.requestArgs.put(name, q);
  }

  /** Returns the args of the Query object with the given name. */
  protected Optional<QueryFillStrategy> getArg(String name) {
    return Optional.ofNullable(this.requestArgs.get(name));
  }

  public void setTty(TTY tty) {
    this.tty.add(tty);
  }

  public Optional<TTY> getTty() {
    return this.tty.isEmpty() ? Optional.empty() : Optional.of(this.tty.get(this.tty.size() - 1));
  }

  public void popTty() {
    if (!this.tty.isEmpty()) {
      this.tty.remove(this.tty.size() - 1);
    }
  }

  public void setResponseStrategy(String name, ShellResponseStrategy s) {
    this.strats.put(name, s);
  }

  public void setDefaultResponseStrategy(ShellResponseStrategy s) {
    this.defaultStrat = s;
  }
}
