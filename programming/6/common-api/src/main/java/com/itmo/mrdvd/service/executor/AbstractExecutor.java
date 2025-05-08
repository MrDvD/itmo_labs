package com.itmo.mrdvd.service.executor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/** A wrapper class for a map of commands & cached queries. */
public abstract class AbstractExecutor {
  protected final Map<String, Command<?>> commands;
  protected final Map<String, CommandMeta> cachedCommands;

  public AbstractExecutor(
      Map<String, Command<?>> commands, Map<String, CommandMeta> cachedCommands) {
    this.commands = commands;
    this.cachedCommands = cachedCommands;
  }

  public void setCache(CommandMeta q) {
    this.cachedCommands.put(q.getCmd(), q);
  }

  public Optional<CommandMeta> getCache(String name) {
    return this.cachedCommands.containsKey(name)
        ? Optional.of(this.cachedCommands.get(name))
        : Optional.empty();
  }

  public Set<String> getCachedKeys() {
    return this.cachedCommands.keySet();
  }

  public void clearCache() {
    this.cachedCommands.clear();
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
      throw new IllegalArgumentException(String.format("Команда \"%s\" не найдена.", name));
    }
    return this.commands.get(name).execute(params);
  }
}
