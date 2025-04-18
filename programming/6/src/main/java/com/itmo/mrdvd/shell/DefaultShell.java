package com.itmo.mrdvd.shell;

import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.DataInputDevice;
import com.itmo.mrdvd.device.input.InteractiveInputDevice;
import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.executor.queries.FetchAllQuery;
import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.ClientProxy;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DefaultShell implements Shell {
  protected final ClientProxy proxy;
  protected final DataInputDevice in;
  protected final OutputDevice out;
  protected final Map<String, Query> cachedQueries;
  protected final Map<String, Command> shellCommands;
  private boolean isOpen;

  public DefaultShell(ClientProxy proxy, DataInputDevice in, OutputDevice out) {
    this(proxy, in, out, new HashMap<>(), new HashMap<>());
  }

  public DefaultShell(
      ClientProxy proxy,
      DataInputDevice in,
      OutputDevice out,
      Map<String, Query> cachedQueries,
      Map<String, Command> shellCommands) {
    this.proxy = proxy;
    this.in = in;
    this.out = out;
    this.cachedQueries = cachedQueries;
    this.shellCommands = shellCommands;
    this.isOpen = false;
  }

  @Override
  public ClientProxy getProxy() {
    return this.proxy;
  }

  @Override
  public DataInputDevice getIn() {
    return this.in;
  }

  @Override
  public OutputDevice getOut() {
    return this.out;
  }

  /** Returns the cached query with the mentioned name. */
  @Override
  public Optional<Query> getQuery(String name) {
    return Optional.ofNullable(this.cachedQueries.get(name));
  }

  /**
   * Fetches the info about queries from the Proxy server and caches it. Also gets the JavaScript
   * files for params validation.
   *
   * <p>Should be launched either upon Shell start or as independent command.
   */
  @Override
  public void fetchQueries() {
    getProxy().send(new FetchAllQuery());
    // get payload and cache queries
  }

  @Override
  public void processLine() throws IOException {
    Optional<String> cmdName = getIn().readToken();
    if (cmdName.isEmpty()) {
      getIn().skipLine();
      return;
    }
    Optional<Query> q = getQuery(cmdName.get());
    if (q.isPresent()) {
      // ...
    } else {
      // ...
    }

    // Optional<Command> cmd = getCommand(cmdName.get());
    // if (cmd.isPresent()) {
    //   if (!cmd.get().hasParams()) {
    //     getIn().skipLine();
    //   }
    //   cmd.get().execute();
    //   return cmd;
    // }
    // return Optional.empty();
  }

  @Override
  public void open() {
    this.isOpen = true;
    while (this.isOpen) {
      if (InteractiveInputDevice.class.isInstance(getIn())) {
        ((InteractiveInputDevice) getIn()).write("> ");
      }
      while (!this.in.hasNext()) {
        this.in.closeIn();
        this.isOpen = false;
        return;
      }
      Optional<Query> q = Optional.empty();
      try {
        processLine();
      } catch (IOException e) {
        close();
        continue;
      }
      if (q.isEmpty()) {
        getOut()
            .writeln(
                "[ERROR] Команда '%s' не найдена: введите 'help' для просмотра списка доступных команд.");
      }
    }
  }

  @Override
  public Optional<Command> getCommand(String name) {
    return Optional.ofNullable(this.shellCommands.get(name));
  }

  @Override
  public void close() {
    this.isOpen = false;
  }

  @Override
  public Set<String> getShellCommandKeys() {
    return this.shellCommands.keySet();
  }

  @Override
  public Set<String> getQueryKeys() {
    return this.cachedQueries.keySet();
  }

  // @Override
  // public CollectionShell forkSubshell(DataInputDevice in, OutputDevice out) {
  //   CollectionShell subshell = new CollectionShell<>(in, out);
  //   for (Command cmd : this) {
  //     subshell.addCommand(cmd.setShell(subshell));
  //   }
  //   return subshell;
  // }
}
