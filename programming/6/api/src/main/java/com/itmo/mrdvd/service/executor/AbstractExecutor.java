package com.itmo.mrdvd.service.executor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.itmo.mrdvd.proxy.Query;

/** A wrapper class for a map of commands & cached queries. */
public abstract class AbstractExecutor {
  protected final Map<String, Command<?>> commands;
  protected final Map<String, Query> cachedQueries;

  public AbstractExecutor(Map<String, Command<?>> commands, Map<String, Query> cachedQueries) {
    this.commands = commands;
    this.cachedQueries = cachedQueries;
  }

  public void setQuery(Query q) {
    this.cachedQueries.put(q.getCmd(), q);
  }

  public Optional<Query> getQuery(String name) {
    return this.cachedQueries.containsKey(name)
        ? Optional.of(this.cachedQueries.get(name))
        : Optional.empty();
  }

  public Set<String> getCachedQueryKeys() {
    return this.cachedQueries.keySet();
  }

  public void setCommand(Command<?> command) {
    this.commands.put(command.name(), command);
  }

  public Optional<Command<?>> getCommand(String name) {
    return this.commands.containsKey(name)
        ? Optional.of(this.commands.get(name))
        : Optional.empty();
  }

  public Set<String> getCommandKeys() {
    return this.commands.keySet();
  }

  // public void getArgsInstruction(String name) {}

  public Object processCommand(String name, List<Object> params) throws IllegalArgumentException {
    if (!this.commands.containsKey(name) || this.commands.get(name) == null) {
      throw new IllegalArgumentException(String.format("Команда не найдена: ", name));
    }
    return this.commands.get(name).execute(params);
  }
}
