package com.itmo.mrdvd.executor;

import java.util.Map;

import com.itmo.mrdvd.executor.commands.Command;

public class AbstractExecutor {
  protected final Map<String, Command<?>> commands;

  public AbstractExecutor(Map<String, Command<?>> commands) {
    this.commands = commands;
  }

  public Map<String, Command<?>> getCommands() {
    return this.commands;
  }
}
