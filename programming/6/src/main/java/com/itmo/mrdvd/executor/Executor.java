package com.itmo.mrdvd.executor;

import java.util.Optional;

import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.executor.queries.Query;

public interface Executor {
  public void setCommand(Command cmd) throws IllegalArgumentException;

  public Optional<Command> getCommand(String name);

  public void processQuery(Query q) throws RuntimeException;
}
