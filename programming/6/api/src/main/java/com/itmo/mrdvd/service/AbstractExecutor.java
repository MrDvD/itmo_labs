package com.itmo.mrdvd.service;

import java.util.Map;

import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.executor.commands.CommandWithParams;

/**
 * A wrapper class for a map of commands.
 */
public abstract class AbstractExecutor implements Service {
  protected final Map<String, Command<?>> commands;

  public AbstractExecutor(Map<String, Command<?>> commands) {
    this.commands = commands;
  }

  public void setCommand(Command<?> command) {
    commands.put(command.name(), command);
  }

  public <T> T processCommand(String name, Object params) throws IllegalArgumentException {
    if (!commands.containsKey(name) || commands.get(name) == null) {
      throw new IllegalArgumentException(String.format("Команда не найдена: ", name));
    }
    Command<T> cmd = (Command) commands.get(name);
    if (cmd instanceof CommandWithParams<T> cmdParams) {
      return cmdParams.withParams(params).execute();
    }
    return cmd.execute();
  }
}
