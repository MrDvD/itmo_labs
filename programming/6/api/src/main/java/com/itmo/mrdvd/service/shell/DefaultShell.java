package com.itmo.mrdvd.service.shell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.device.input.InteractiveInputDevice;
import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.Proxy;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.service.shell.query_fill_strategy.QueryFillStrategy;
import com.itmo.mrdvd.service.shell.response_strategy.ShellResponseStrategy;

public class DefaultShell extends AbstractShell {
  protected final Proxy proxy;
  protected final Function<String, Query> query;
  private boolean isOpen;

  public DefaultShell(Proxy proxy, Function<String, Query> query) {
    this(proxy, query, new ArrayList<>(), new HashMap<>(), new HashMap<>());
  }

  public DefaultShell(
      Proxy proxy,
      Function<String, Query> query,
      List<TTY> tty,
      Map<String, QueryFillStrategy> args,
      Map<String, ShellResponseStrategy> strats) {
    super(tty, args, strats);
    this.proxy = proxy;
    this.query = query;
  }

  /** Processes the passed Response. */
  protected void processResponse(Response r) throws IllegalStateException {
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
  protected Optional<Query> fillQuery(String cmd) throws IOException {
    Query q = this.query.apply(cmd);
    Optional<QueryFillStrategy> arg = getArg(cmd);
    if (arg.isPresent()) {
      q = arg.get().fillArgs(q);
    }
    return Optional.of(q);
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
    Optional<Query> q = fillQuery(cmdName.get());
    if (q.isEmpty()) {
      getTty().get().getOut().writeln(String.format("Команда не найдена: %s", cmdName.get()));
      return;
    }
    Response r = this.proxy.processQuery(q.get());
    processResponse(r);
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
        stop();
        return;
      }
      try {
        processLine();
      } catch (IOException e) {
        stop();
      } catch (RuntimeException e) {
        getTty().get().getOut().writeln(e.getMessage());
      }
    }
  }

  @Override
  public void stop() {
    this.isOpen = false;
  }
}
