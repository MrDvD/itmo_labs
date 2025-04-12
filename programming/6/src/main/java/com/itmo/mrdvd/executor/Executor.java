package com.itmo.mrdvd.executor;

import java.util.Optional;

import com.itmo.mrdvd.command.Command;

public interface Executor {
  public void setCommand(Command cmd);
  public Optional<Command> getCommand(String name);
  public void processQuery(Query q) throws RuntimeException;
}
