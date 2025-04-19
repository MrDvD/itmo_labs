package com.itmo.mrdvd.shell;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.DataInputDevice;
import com.itmo.mrdvd.device.input.InteractiveInputDevice;
import com.itmo.mrdvd.executor.commands.ShellCommand;
import com.itmo.mrdvd.executor.queries.FetchAllQuery;
import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.ClientProxy;

public class ProxyShell implements Shell {
  protected final ClientProxy proxy;
  protected final DataInputDevice in;
  protected final OutputDevice out;
  protected final Map<String, Query> cachedQueries;
  protected final Map<String, ShellCommand> shellCommands;
  private boolean isOpen;

  public ProxyShell(ClientProxy proxy, DataInputDevice in, OutputDevice out) {
    this(proxy, in, out, new HashMap<>(), new HashMap<>());
  }

  public ProxyShell(
      ClientProxy proxy,
      DataInputDevice in,
      OutputDevice out,
      Map<String, Query> cachedQueries,
      Map<String, ShellCommand> shellCommands) {
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

  /** 
   * Returns the cached query with the mentioned name.
   */
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

  /**
   * Processes the input query or command.
   * Returns the input keyword if it wasn't processed.
   */
  @Override
  public Optional<String> processLine() throws IOException {
    Optional<String> cmdName = getIn().readToken();
    if (cmdName.isEmpty()) {
      getIn().skipLine();
      return Optional.empty();
    }
    Optional<ShellCommand> c = getCommand(cmdName.get());
    if (c.isPresent()) {
      if (!c.get().hasParams()) {
        getIn().skipLine();
      }
      c.get().execute();
      return Optional.empty();
    }
    Optional<Query> q = getQuery(cmdName.get());
    if (q.isPresent()) {
      // send the query to the server
      // and process the result.
    }
    return cmdName;
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
      Optional<String> cmd;
      try {
        cmd = processLine();
      } catch (IOException e) {
        close();
        continue;
      }
      if (cmd.isPresent()) {
        getOut()
            .writeln(String.format("[ERROR] Команда '%s' не найдена: введите 'help' для просмотра списка доступных команд.", cmd.get()));
      }
    }
  }

  @Override
  public void setCommand(ShellCommand cmd) throws IllegalArgumentException {
    if (cmd == null) {
      throw new IllegalArgumentException("Command не может быть null.");
    }
    this.shellCommands.put(cmd.name(), cmd);
  }

  @Override
  public Optional<ShellCommand> getCommand(String name) {
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

  @Override
  public ProxyShell forkSubshell(DataInputDevice in, OutputDevice out) {
    ProxyShell subshell = new ProxyShell(proxy, in, out);
    for (String cmdName : this.getShellCommandKeys()) {
      Optional<ShellCommand> cmd = this.getCommand(cmdName);
      subshell.setCommand(cmd.get());
    }
    return subshell;
  }
}
