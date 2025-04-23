package com.itmo.mrdvd.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.executor.commands.CommandWithParams;
import com.itmo.mrdvd.executor.queries.Query;

public class DefaultExecutor implements Executor {
  protected final Map<String, Command> commands;

  public DefaultExecutor() {
    this.commands = new HashMap<>();
  }

  public DefaultExecutor(Map<String, Command> commands) {
    this.commands = commands;
  }

  @Override
  public void setCommand(Command cmd) throws IllegalArgumentException {
    if (cmd == null) {
      throw new IllegalArgumentException("Нельзя установить null в качестве команды.");
    }
    this.commands.put(cmd.name(), cmd);
  }

  @Override
  public Optional<Command> getCommand(String name) {
    return this.commands.containsKey(name)
        ? Optional.empty()
        : Optional.of(this.commands.get(name));
  }

  @Override
  public void processQuery(Query q) throws IllegalArgumentException {
    Optional<Command> cmd = getCommand(q.getCmd());
    if (cmd.isEmpty()) {
      throw new IllegalArgumentException("Не удалось распознать запрос.");
    }
    if (cmd.get() instanceof CommandWithParams cmdWithParams) {
      cmdWithParams.withParams(q.getParams()).execute();
    } else {
      cmd.get().execute();
    }
  }
}
