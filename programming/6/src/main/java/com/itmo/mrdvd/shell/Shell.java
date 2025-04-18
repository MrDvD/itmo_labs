package com.itmo.mrdvd.shell;

import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.DataInputDevice;
import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.ClientProxy;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public interface Shell {
  public DataInputDevice getIn();

  public OutputDevice getOut();

  public ClientProxy getProxy();

  public void fetchQueries();

  public Optional<Query> getQuery(String name);

  public Optional<Command> getCommand(String name);

  public Set<String> getShellCommandKeys();

  public Set<String> getQueryKeys();

  public void processLine() throws IOException;

  public Shell forkSubshell(DataInputDevice in, OutputDevice out);

  public void open();

  public void close();
}
