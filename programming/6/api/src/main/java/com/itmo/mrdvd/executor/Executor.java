package com.itmo.mrdvd.executor;

import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.executor.queries.Query;
import java.util.List;
import java.util.Optional;

public interface Executor {
  public void setCommand(Command cmd) throws IllegalArgumentException;

  public Optional<Command> getCommand(String name);

  public void processQuery(Query q, List<?> prefixParams) throws RuntimeException;
}
