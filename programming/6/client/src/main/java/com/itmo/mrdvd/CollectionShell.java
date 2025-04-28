package com.itmo.mrdvd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.device.input.InteractiveInputDevice;
import com.itmo.mrdvd.executor.commands.response.Response;
import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.Proxy;

public class CollectionShell extends AbstractShell {
  protected final Proxy proxy;
  protected final Map<String, Query> cachedQueries;
  private boolean isOpen;

  public CollectionShell(Proxy proxy) {
    this(proxy, new ArrayList<>(), new HashMap<>(), new HashMap<>());
  }

  public CollectionShell(Proxy proxy, List<TTY> tty, Map<String, Object> args, Map<String, Query> cachedQueries) {
    super(tty, args);
    this.proxy = proxy;
    this.cachedQueries = cachedQueries;
  }

  /**
   * Processes the passed Response.
   */
  protected void processResponse(Response r) throws IllegalStateException {
    if (getTty().isEmpty()) {
      throw new IllegalStateException("Не предоставлен TTY для вывода ответа.");
    }
    if (r.getBody() != null) {
      getTty().get().getOut().writeln(r.getBody());
    }
  }

  /**
   * Wraps the user input into Query.
   */
  protected Optional<Query> fillQuery(String cmd) {
    Optional<Query> qRaw = getQuery(cmd);
    if (qRaw.isEmpty()) {
      return Optional.empty(); 
    }
    Query q = qRaw.get();
    Optional<Object> arg = getArg(cmd);
    // here i also should set other args from user input (via JS & validation stuff)
    if (arg.isPresent()) {
      q.setArgs(arg.get());
    }
    return Optional.of(q);
  }

  /**
   * Gets the cached query with the given name.
   */
  protected Optional<Query> getQuery(String name) {
    return this.cachedQueries.containsKey(name) ? Optional.of(this.cachedQueries.get(name)) : Optional.empty();
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
    setArg("exit", this);
    // 
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
      }
    }
  }

  @Override
  public void stop() {
    this.isOpen = false;
  }
}
