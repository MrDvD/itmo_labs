package com.itmo.mrdvd.service.shell;

import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.device.input.InteractiveInputDevice;
import com.itmo.mrdvd.proxy.Proxy;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.service.shell.queryFillStrategy.QueryFillStrategy;
import com.itmo.mrdvd.service.shell.responseStrategy.ShellResponseStrategy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DefaultShell extends AbstractShell {
  protected final Proxy proxy;
  private boolean isOpen;

  public DefaultShell(Proxy proxy) {
    this(proxy, new ArrayList<>(), new HashMap<>(), new HashMap<>(), new HashSet<>());
  }

  public DefaultShell(
      Proxy proxy,
      List<TTY> tty,
      Map<String, QueryFillStrategy> args,
      Map<String, ShellResponseStrategy> strats,
      Set<String> usedTtys) {
    super(tty, args, strats, usedTtys);
    this.proxy = proxy;
  }

  /** Processes the passed Response. */
  protected void processResponse(ServiceQuery r) throws IllegalStateException {
    if (this.strats.containsKey(r.getName())) {
      this.strats.get(r.getName()).make(r);
    } else {
      if (this.defaultStrat == null) {
        throw new IllegalStateException("Не задана стандартная стратегия обработки ответа.");
      }
      this.defaultStrat.make(r);
    }
  }

  /** Wraps the user input into Query. */
  protected ServiceQuery fillQuery(String cmd) throws IOException {
    ServiceQuery q = ServiceQuery.of(cmd, List.of());
    Optional<QueryFillStrategy> arg = getArg(cmd);
    if (arg.isPresent()) {
      q = arg.get().fillArgs(q);
    } else {
      getTty().get().getIn().skipLine();
    }
    return q;
  }

  protected void processLine() throws IOException, IllegalStateException {
    if (getTty().isEmpty()) {
      throw new IllegalStateException("Не предоставлен TTY для обработки ввода/вывода.");
    }
    Optional<String> cmdName = getTty().get().getIn().readToken();
    if (cmdName.isEmpty()) {
      getTty().get().getIn().skipLine();
      return;
    }
    Optional<ServiceQuery> q = this.proxy.processQuery(fillQuery(cmdName.get()));
    if (q.isPresent()) {
      processResponse(q.get());
    }
  }

  @Override
  public void start() {
    if (getTty().isEmpty()) {
      throw new IllegalStateException("Не предоставлен TTY для работы интерпретатора.");
    }
    this.isOpen = true;
    while (this.isOpen) {
      if (getTty().get().getIn() instanceof InteractiveInputDevice back) {
        back.write("> ");
      }
      while (!getTty().get().getIn().hasNext()) {
        getTty().get().getIn().closeIn();
        popTty();
        if (getTty().isEmpty()) {
          stop();
          return;
        } else if (getTty().get().getIn() instanceof InteractiveInputDevice back) {
          back.write("> ");
        }
      }
      try {
        processLine();
      } catch (IOException e) {
        stop();
      }
    }
  }

  @Override
  public void stop() {
    this.isOpen = false;
  }
}
