package com.itmo.mrdvd.executor;

import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.executor.commands.CommandWithParams;
import com.itmo.mrdvd.executor.queries.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class DefaultExecutor implements Executor {
  protected final Map<String, Command<?>> commands;

  public DefaultExecutor() {
    this.commands = new HashMap<>();
  }

  public DefaultExecutor(Map<String, Command<?>> commands) {
    this.commands = commands;
  }

  @Override
  public void setCommand(Command<?> cmd) throws IllegalArgumentException {
    if (cmd == null) {
      throw new IllegalArgumentException("Нельзя установить null в качестве команды.");
    }
    this.commands.put(cmd.name(), cmd);
  }

  @Override
  public Optional<Command<?>> getCommand(String name) {
    return this.commands.containsKey(name)
        ? Optional.of(this.commands.get(name))
        : Optional.empty();
  }

  @Override
  public Query processQuery(Query q, List<?> prefixParams) throws IllegalArgumentException {
    Optional<Command<?>> cmd = getCommand(q.getCmd());
    // return error query if command not found
    if (cmd.isEmpty()) {
      throw new IllegalArgumentException("Не удалось распознать запрос.");
    }
    if (cmd.get() instanceof CommandWithParams<?> cmdWithParams) {
      List<?> resultParams = List.of();
      if (prefixParams != null) {
        resultParams = prefixParams;
      }
      resultParams = Stream.concat(resultParams.stream(), q.getParams().stream()).toList();
      Object response = cmdWithParams.withParams(resultParams).execute();
      if (!(response instanceof Void)) {
        // return query based on response
      } else {
        // return success query
        // (error query will be sent higher in proxy level)
      }
      // how to return result if command fetches data for client?
    } else {
      cmd.get().execute();
    }
  }
}
