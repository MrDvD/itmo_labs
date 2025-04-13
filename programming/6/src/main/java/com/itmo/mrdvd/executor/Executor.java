package com.itmo.mrdvd.executor;

import com.itmo.mrdvd.executor.command.Command;
import com.itmo.mrdvd.executor.queries.Query;
import java.util.Optional;

public interface Executor {
  public void setCommand(Command cmd);

  public Optional<Command> getCommand(String name);

  public void processQuery(Query q) throws RuntimeException;
}
