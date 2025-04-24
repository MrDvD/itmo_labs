package com.itmo.mrdvd.shell;

import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.DataInputDevice;
import com.itmo.mrdvd.executor.commands.shell.ShellCommand;
import com.itmo.mrdvd.executor.queries.Query;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public interface Shell {
  public DataInputDevice getIn();

  public OutputDevice getOut();

  /** Sets / updates the cached query manually. */
  public void setQuery(Query q);

  /** Returns the cached query with the mentioned name. */
  public Optional<Query> getQuery(String name);

  public void setCommand(ShellCommand cmd) throws IllegalArgumentException;

  public Optional<ShellCommand> getCommand(String name);

  public Set<String> getShellCommandKeys();

  public Set<String> getQueryKeys();

  /** Processes the input query or command. Returns the input keyword if it wasn't processed. */
  public Optional<String> processLine() throws IOException;

  public Shell forkSubshell(DataInputDevice in, OutputDevice out);

  public void open();

  public void close();
}
