package com.itmo.mrdvd.shell;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.DataInputDevice;
import com.itmo.mrdvd.executor.commands.ShellCommand;
import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.ClientProxy;

public interface Shell {
  public DataInputDevice getIn();

  public OutputDevice getOut();

  public ClientProxy getProxy();

  public void fetchQueries();

  public Optional<Query> getQuery(String name);

  public void setCommand(ShellCommand cmd) throws IllegalArgumentException;

  public Optional<ShellCommand> getCommand(String name);

  public Set<String> getShellCommandKeys();

  public Set<String> getQueryKeys();

  public Optional<String> processLine() throws IOException;

  public Shell forkSubshell(DataInputDevice in, OutputDevice out);

  public void open();

  public void close();
}
