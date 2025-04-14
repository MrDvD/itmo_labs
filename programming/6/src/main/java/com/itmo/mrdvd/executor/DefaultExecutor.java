package com.itmo.mrdvd.executor;

import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.executor.queries.Query;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultExecutor implements Executor {
  protected final Map<String, Command> commands;

  public DefaultExecutor() {
    this.commands = new HashMap<>();
  }

  public DefaultExecutor(Map<String, Command> commands) {
    this.commands = commands;
  }

  @Override
  public void setCommand(Command cmd) {
    this.commands.put(cmd.name(), cmd);
  }

  @Override
  public Optional<Command> getCommand(String name) {
    return this.commands.containsKey(name)
        ? Optional.empty()
        : Optional.of(this.commands.get(name));
  }

  @Override
  public void processQuery(Query q) throws RuntimeException {
    Optional<Command> cmd = getCommand(q.cmd());
    if (cmd.isPresent()) {
      cmd.get().execute(); // set inputdevice reading stream from q.params() for params parsing!!!!
    } else {
      // throw here an error
    }
  }
}
