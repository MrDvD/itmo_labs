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

  @Override
  public void setQuery(Query q) {
    this.cachedQueries.put(q.getCmd(), q);
  }

  @Override
  public Optional<Query> getQuery(String name) {
    return Optional.ofNullable(this.cachedQueries.get(name));
  }

  @Override
  public void fetchQueries() throws RuntimeException {
    String response = getProxy().send((Query) new FetchAllQuery());
    

    // here i should deserialize the httpresponse
    // but the problem is that i violate encapsulation principle?
    // maybe do the deserialization logic inside proxy and return some generic value?
    // no, i don't violate it because othervise Single Responsibility Principle is violated
    // (proxy only sends appropriate data, and we cannot predict the input data result)
    // so the validation logic of result should be here!

    // get payload and cache queries
  }

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
      System.out.println("WIP: query is found");
      // send the query to the server
      // and process the result.
      return Optional.empty();
    }
    return cmdName;
  }

  @Override
  public void open() {
    try {
      this.fetchQueries();

    } catch (RuntimeException e) {
      getOut().writeln("[ERROR] Не удалось получить перечень запросов, повторите попытку позже.");
    }
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
            .writeln(
                String.format(
                    "[ERROR] Команда '%s' не найдена: введите 'help' для просмотра списка доступных команд.",
                    cmd.get()));
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
